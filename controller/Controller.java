package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    public Controller() {

    }

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
            new Error(ex.getMessage());
        }

        return cinemaList;
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
            new Error(ex.getMessage());
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
            File dir = new File(System.getProperty("java.io.tmpdir") + "/BioskopHub");
            System.out.println(dir.getAbsolutePath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(System.getProperty("java.io.tmpdir") + "/BioskopHub/lastdir.txt");
            System.out.println(file.getAbsolutePath());
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
}
