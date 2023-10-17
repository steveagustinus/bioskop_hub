package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import model.Cinema;

public class Controller {
    public Controller() { }

    // return list of string nama cinema
    public ArrayList<String> getCinemaList() {
        ArrayList<String> cinemaList = new ArrayList<String>();
        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            Statement statement = conn.createStatement();
            ResultSet rows = statement.executeQuery(
                "SELECT `nama` FROM `cinema`"
            );

            while (rows.next()) {
                cinemaList.add(rows.getString("nama"));
            }

            rows.close();
            statement.close();
            conn.close();
        } catch (Exception ex) {
            new ErrorLogger(ex.getMessage());
        }

        return cinemaList;
    }

    public boolean isCinemaExists(String idCinema) {
        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM `cinema` WHERE `id_cinema`='" + idCinema + "'"
            );

            boolean exists = false;
            if (result.isBeforeFirst()) {
                exists = true;
            }

            result.close();
            statement.close();
            conn.close();

            return exists;
        } catch (Exception ex) {
            System.out.println(ex);
            new ErrorLogger(ex.getMessage());
            return false;
        }
    }

    public Cinema getCinemaById(String idCinema) {
        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM `cinema` WHERE `id_cinema`='" + idCinema + "'"
            );

            result.next();

            File fotoCinema = new File(System.getProperty("java.io.tmpdir") + "/BioskopHub/img.png");
            fotoCinema.createNewFile();

            Path target = fotoCinema.toPath();
            Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

            fotoCinema = new File(System.getProperty("java.io.tmpdir") + "/BioskopHub/img.png");

            Cinema cinema = new Cinema(
                idCinema,
                result.getString("nama"),
                result.getString("alamat"),
                result.getString("kota"),
                fotoCinema,
                null
            );

            result.close();
            statement.close();
            conn.close();

            return cinema;
        } catch (Exception ex) {
            new ErrorLogger(ex.getMessage());
            return null;
        }
    }

    public int addNewCinema(String idCinema, String nama, String alamat, String kota, File fotoCinema) {
        if (idCinema == null || idCinema.equals("") || idCinema.length() != 10) { return -1; }
        if (nama == null || nama.equals("")) { return -2; }
        if (alamat == null || alamat.equals("")) { return -3; }
        if (kota == null || kota.equals("")) { return -4; }
        if (fotoCinema == null) { return -5; }

        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            String sql = "INSERT INTO `cinema` (`id_cinema`, `nama`, `kota`, `alamat`, `img`)" +
                "VALUES (?, ?, ?, ?, ?)";

            conn.setAutoCommit(false);

            try (
                FileInputStream fis = new FileInputStream(fotoCinema);
                PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                ps.setString(1, idCinema);
                ps.setString(2, nama);
                ps.setString(3, kota);
                ps.setString(4, alamat);
                ps.setBinaryStream(5, fis, (int) fotoCinema.length());
                ps.executeUpdate();
                conn.commit();
                ps.close();
            }

            conn.close();

            return 0;
        } catch (Exception ex) {
            new ErrorLogger(ex.getMessage());
            System.out.println(ex.getMessage());
            return -99;
        }
    }

    public int editCinema(String idCinema, String nama, String alamat, String kota, File fotoCinema) {
        if (idCinema == null || idCinema.equals("")) { return -1; }
        
        boolean empty_nama = nama == null || nama.equals("");
        boolean empty_alamat = alamat == null || alamat.equals("");
        boolean empty_kota = kota == null || kota.equals("");
        boolean empty_foto = fotoCinema == null;

        if (empty_nama && empty_alamat && empty_kota && empty_foto) { return -2; }

        String sql = "UPDATE `cinema` SET ";
        if (!empty_nama) { sql += "`nama` = ?, "; }
        if (!empty_alamat) { sql += "`alamat` = ?, "; }
        if (!empty_kota) { sql += "`kota` = ?, "; }
        if (!empty_foto) { sql += "`img` = ?,"; }
        sql = sql.substring(0, sql.length() - 1);
        sql += " WHERE `id_cinema` = ?";

        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            conn.setAutoCommit(false);

            try (
                FileInputStream fis = new FileInputStream(fotoCinema);
                PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                int count = 1;
                if (!empty_nama) { ps.setString(count, nama); count++; }
                if (!empty_alamat) { ps.setString(count, alamat); count++; }
                if (!empty_kota) { ps.setString(count, kota); count++; }
                if (!empty_foto) { ps.setBinaryStream(count, fis, (int) fotoCinema.length()); count++; }
                ps.setString(count, idCinema);
                
                ps.executeUpdate();
                conn.commit();
                ps.close();
            }

            conn.close();

            return 0;
        } catch (Exception ex) {
            new ErrorLogger(ex.getMessage());
            System.out.println(ex.getMessage());
            return -99;
        }
    }

    public String getLastOpenedDirectory() {
        try {
            Scanner scanner = new Scanner(new File(System.getProperty("java.io.tmpdir") + "/BioskopHub/lastdir.txt"));
            return scanner.nextLine();
        } catch (FileNotFoundException fileNotFoundEx) {
            return "";
        }
    }

    public int saveLastOpenedDirectory(String path) {
        try {
            File file = new File(System.getProperty("java.io.tmpdir") + "/BioskopHub/lastdir.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(path);
            fileWriter.close();
            return 0; // Success
        } catch (IOException ioEx) {
            System.out.println(ioEx);
            return -1;
        } catch (Exception ex) {
            System.out.println(ex);
            return -99;
        }
    }

    public void programStart() {
        File dir = new File(System.getProperty("java.io.tmpdir") + "/BioskopHub");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        
    }
}
