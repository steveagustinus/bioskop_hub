package src.controller;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import src.model.Cinema;
import src.model.movie.Movie;
import src.model.seat.Seat;
import src.model.seat.SeatStatusInterface;
import src.model.studio.Studio;
import src.model.studio.StudioClassEnum;
import src.model.studio.StudioTypeInterface;
import src.model.user.Admin;
import src.model.user.Customer;
import src.model.user.MembershipCustomer;
import src.model.user.User;

public class Controller {
    static DatabaseHandler conn = new DatabaseHandler();

    public Controller() {
    }

    // Seat area
    public int getLastSeatId() {
        int lastId = -1;
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `id_seat` FROM `seat` ORDER BY `id_seat` DESC LIMIT 1;");

            if (!result.isBeforeFirst()) {
                return 0;
            }

            result.next();

            lastId = Integer.parseInt(result.getString("id_seat"));

            result.close();
            statement.close();
            conn.close();

            return lastId;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -1;
        }
    }

    public int generateSeat(Studio studio) {

        Seat[][] seats = new Seat[1][1];
        switch (studio.getStudioClass()) {
            case REGULER:
                seats = new Seat[15][9];
                break;
            case LUXE:
                seats = new Seat[8][8];
                break;
            case JUNIOR:
                seats = new Seat[10][9];
                break;
            case VIP:
                seats = new Seat[5][5];
                break;
        }

        int idSeat = getLastSeatId() + 1;

        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = new Seat(
                        String.valueOf(idSeat),
                        ((char) (i + 65)) + ((j + 1) < 10 ? "0" + String.valueOf(j + 1) : String.valueOf(j + 1)),
                        SeatStatusInterface.AVAILABLE);
                idSeat++;
            }
        }

        // Insert seats into database
        String sql = "INSERT INTO `seat` (`id_seat`, `id_studio`, `kode`)" +
                "VALUES ";

        for (Seat[] arrSeat : seats) {
            for (Seat seat : arrSeat) {
                sql += "('" + seat.getIdSeat() + "', '" + studio.getIdStudio() + "', '" + seat.getSeatCode() + "'),";
            }
        }

        sql = sql.substring(0, sql.length() - 1) + ";";

        try {
            conn.open();
            conn.connection.setAutoCommit(false);
            PreparedStatement ps = conn.connection.prepareStatement(sql);

            ps.executeUpdate();
            conn.connection.commit();
            ps.close();

            conn.close();
            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    // Jadwal area
    // public Seat[] GenerateSeat(Jadwal jadwal) {
    // return null;
    // }

    // Studio area
    public Studio getStudioById(String idStudio) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `studio` WHERE `id_studio`='" + idStudio + "';");

            result.next();

            Studio studio = new Studio(
                    idStudio,
                    result.getString("id_cinema"),
                    getStudioClassEnum(result.getString("studio_class")),
                    result.getInt("studio_type"));

            result.close();
            statement.close();
            conn.close();

            return studio;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Studio> getStudio(String idCinema, boolean getJadwalData) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `studio` WHERE `id_cinema`='" + idCinema + "'");

            if (!result.isBeforeFirst()) {
                return null;
            }

            ArrayList<Studio> studioList = new ArrayList<Studio>();
            while (result.next()) {
                Studio studio = new Studio(
                        result.getString("id_studio"),
                        result.getString("id_cinema"),
                        getStudioClassEnum(result.getString("studio_class")),
                        result.getInt("studio_type"));
                studioList.add(studio);
            }

            result.close();
            statement.close();
            conn.close();

            return studioList;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public StudioClassEnum getStudioClassEnum(String studioClass) {
        switch (studioClass.toUpperCase()) {
            case "VIP":
                return StudioClassEnum.VIP;
            case "JUNIOR":
                return StudioClassEnum.JUNIOR;
            case "LUXE":
                return StudioClassEnum.LUXE;
            case "REGULAR":
                return StudioClassEnum.REGULER;
        }
        return null;
    }

    public String[] getListStudioClass() {
        return new String[] { "REGULAR", "LUXE", "JUNIOR", "VIP" };
    }

    public String getStudioClassString(StudioClassEnum studioClass) {
        switch (studioClass) {
            case VIP:
                return "VIP";
            case JUNIOR:
                return "Junior";
            case LUXE:
                return "Luxe";
            case REGULER:
                return "Regular";
        }
        return "";
    }

    public String[] getListStudioType() {
        return new String[] { "2D", "3D", "4D", "5D" };
    }

    public int getStudioType(String studioType) {
        switch (studioType) {
            case "2D":
                return StudioTypeInterface.TYPE2D;
            case "3D":
                return StudioTypeInterface.TYPE3D;
            case "4D":
                return StudioTypeInterface.TYPE4D;
            case "5D":
                return StudioTypeInterface.TYPE5D;
        }

        return -1;
    }

    public String getStudioTypeString(int studioType) {
        switch (studioType) {
            case StudioTypeInterface.TYPE2D:
                return "2D";
            case StudioTypeInterface.TYPE3D:
                return "3D";
            case StudioTypeInterface.TYPE4D:
                return "4D";
            case StudioTypeInterface.TYPE5D:
                return "5D";
        }

        return "";
    }

    public int addNewStudio(String idStudio, String idCinema, String studioClass, String studioType) {
        return addNewStudio(
                idStudio,
                idCinema,
                getStudioClassEnum(studioClass),
                getStudioType(studioType));
    }

    public int addNewStudio(String idStudio, String idCinema, StudioClassEnum studioClass, int studioType) {
        if (idStudio == null || idStudio.equals("") || idStudio.length() != 10) {
            return -1;
        }
        if (idCinema == null || idCinema.equals("") || idCinema.length() != 10) {
            return -2;
        }
        if (studioClass == null) {
            return -3;
        }
        if (studioType < 0) {
            return -4;
        }

        try {
            conn.open();

            String sql = "INSERT INTO `studio` (`id_studio`, `id_cinema`, `studio_class`, `studio_type`)" +
                    "VALUES (?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            PreparedStatement ps = conn.connection.prepareStatement(sql);

            ps.setString(1, idStudio);
            ps.setString(2, idCinema);
            ps.setString(3, getStudioClassString(studioClass).toUpperCase());
            ps.setInt(4, studioType);

            ps.executeUpdate();
            conn.connection.commit();
            ps.close();

            conn.close();

            // Generate seats on studio creation
            if (generateSeat(new Studio(idStudio, idCinema, studioClass, studioType)) != 0) {
                return -5;
            }

            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    public int editStudio(String idStudio, String idCinema, String studioClass, String studioType) {
        return editStudio(
                idStudio,
                idCinema,
                getStudioClassEnum(studioClass),
                getStudioType(studioType));
    }

    public int editStudio(String idStudio, String idCinema, StudioClassEnum studioClass, int studioType) {
        if (idStudio == null || idStudio.equals("")) {
            return -1;
        }

        if (idCinema == null || idCinema.equals("")) {
            return -2;
        }

        if (studioClass == null) {
            return -3;
        }

        try {
            conn.open();

            String sql = "UPDATE `studio` SET `id_cinema`=?, `studio_class`=?, `studio_type`=? WHERE `id_studio`=?;";
            PreparedStatement ps = conn.connection.prepareStatement(sql);

            ps.setString(1, idCinema);
            ps.setString(2, getStudioClassString(studioClass).toUpperCase());
            ps.setInt(3, studioType);
            ps.setString(4, idStudio);

            ps.executeUpdate();
            ps.close();
            conn.close();

            return 0;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    // Cinema area
    public String[] getCinemaStringList() {
        ArrayList<String> cinemaList = new ArrayList<String>();
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet rows = statement.executeQuery(
                    "SELECT `nama` FROM `cinema`");

            while (rows.next()) {
                cinemaList.add(rows.getString("nama"));
            }

            rows.close();
            statement.close();
            conn.close();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }

        return cinemaList.toArray(new String[cinemaList.size()]);
    }

    public boolean isCinemaExists(String idCinema) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `cinema` WHERE `id_cinema`='" + idCinema + "'");

            boolean exists = false;
            if (result.isBeforeFirst()) {
                exists = true;
            }

            result.close();
            statement.close();
            conn.close();

            return exists;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return false;
        }
    }

    public Cinema getCinemaById(String idCinema, boolean getStudioData) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `cinema` WHERE `id_cinema`='" + idCinema + "'");

            result.next();

            File fotoCinema = new File(Config.Path.TEMP_DIR + "img.png");
            fotoCinema.createNewFile();

            Path target = fotoCinema.toPath();
            Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

            fotoCinema = new File(Config.Path.TEMP_DIR + "img.png");

            Cinema cinema = null;
            if (getStudioData) {
                cinema = new Cinema(
                        idCinema,
                        result.getString("nama"),
                        result.getString("alamat"),
                        result.getString("kota"),
                        fotoCinema,
                        getStudio(idCinema, true));
            } else {
                cinema = new Cinema(
                        idCinema,
                        result.getString("nama"),
                        result.getString("alamat"),
                        result.getString("kota"),
                        fotoCinema,
                        null);
            }

            result.close();
            statement.close();
            conn.close();

            return cinema;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public int addNewCinema(String idCinema, String nama, String alamat, String kota, File fotoCinema) {
        if (idCinema == null || idCinema.equals("") || idCinema.length() != 10) {
            return -1;
        }
        if (nama == null || nama.equals("")) {
            return -2;
        }
        if (alamat == null || alamat.equals("")) {
            return -3;
        }
        if (kota == null || kota.equals("")) {
            return -4;
        }
        if (fotoCinema == null) {
            return -5;
        }

        try {
            conn.open();

            String sql = "INSERT INTO `cinema` (`id_cinema`, `nama`, `kota`, `alamat`, `img`)" +
                    "VALUES (?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            try (
                    FileInputStream fis = new FileInputStream(fotoCinema);
                    PreparedStatement ps = conn.connection.prepareStatement(sql);) {
                ps.setString(1, idCinema);
                ps.setString(2, nama);
                ps.setString(3, kota);
                ps.setString(4, alamat);
                ps.setBinaryStream(5, fis, (int) fotoCinema.length());
                ps.executeUpdate();
                conn.connection.commit();
                ps.close();
            }

            conn.close();

            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    public int editCinema(String idCinema, String nama, String alamat, String kota, File fotoCinema) {
        if (idCinema == null || idCinema.equals("")) {
            return -1;
        }

        boolean empty_nama = nama == null || nama.equals("");
        boolean empty_alamat = alamat == null || alamat.equals("");
        boolean empty_kota = kota == null || kota.equals("");
        boolean empty_foto = fotoCinema == null;

        if (empty_nama && empty_alamat && empty_kota && empty_foto) {
            return -2;
        }

        String sql = "UPDATE `cinema` SET ";
        if (!empty_nama) {
            sql += "`nama` = ?, ";
        }
        if (!empty_alamat) {
            sql += "`alamat` = ?, ";
        }
        if (!empty_kota) {
            sql += "`kota` = ?, ";
        }
        if (!empty_foto) {
            sql += "`img` = ?,";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " WHERE `id_cinema` = ?";

        try {
            conn.open();

            conn.connection.setAutoCommit(false);

            try (
                    FileInputStream fis = new FileInputStream(fotoCinema);
                    PreparedStatement ps = conn.connection.prepareStatement(sql);) {
                int count = 1;
                if (!empty_nama) {
                    ps.setString(count, nama);
                    count++;
                }
                if (!empty_alamat) {
                    ps.setString(count, alamat);
                    count++;
                }
                if (!empty_kota) {
                    ps.setString(count, kota);
                    count++;
                }
                if (!empty_foto) {
                    ps.setBinaryStream(count, fis, (int) fotoCinema.length());
                    count++;
                }
                ps.setString(count, idCinema);

                ps.executeUpdate();
                conn.connection.commit();
                ps.close();
            }

            conn.close();

            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    // Movie area
    public Movie getMovieById(String idMovie) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `movie` WHERE `id_movie`='" + idMovie + "'");

            result.next();

            File fotoMovie = new File(Config.Path.TEMP_DIR + "img.png");
            fotoMovie.createNewFile();

            Path target = fotoMovie.toPath();
            Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

            fotoMovie = new File(Config.Path.TEMP_DIR + "img.png");

            Movie movie = new Movie(
                    idMovie,
                    result.getString("judul"),
                    result.getDate("release_date"),
                    result.getString("director"),
                    result.getInt("language"),
                    result.getInt("durasi"),
                    result.getString("sinopsis"),
                    fotoMovie);

            result.close();
            statement.close();
            conn.close();

            return movie;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public boolean isMovieExists(String idMovie) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `movie` WHERE `id_movie`='" + idMovie + "'");

            boolean exists = false;
            if (result.isBeforeFirst()) {
                exists = true;
            }

            result.close();
            statement.close();
            conn.close();

            return exists;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return false;
        }
    }

    // User area
    private User getUserById(String idUser) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `user` WHERE `id_user`='" + idUser + "'");

            result.next();

            User user = null;
            int userType = result.getInt("user_type");

            switch (userType) {
                case 0:
                    user = new Admin(result.getString("username"), result.getString("password"));
                    break;

                case 1:
                    user = new Customer(
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("profile_name"),
                            result.getString("email"),
                            result.getString("phone_no"),
                            result.getString("address"), null);
                    break;

                case 2:
                    user = new MembershipCustomer(
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("profile_name"),
                            result.getString("email"),
                            result.getString("phone_no"),
                            result.getString("address"),
                            null, result.getInt("poin"));
                    break;
            }

            result.close();
            statement.close();
            conn.close();

            return user;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public User login(String username, String password) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT id_user, user_type FROM `user` WHERE `username`='" + username + "' AND `password`='"
                            + sha256(password) + "'");

            if (!result.isBeforeFirst()) {
                return null;
            }

            result.next();
            return getUserById(result.getString("id_user"));
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public int register(String username, String password, String email, String phoneNumber, String alamat) {
        try {
            int isNameExist = isNameExist(username);
            if (isNameExist == 1) {
                return 0;
            }

            conn.open();
            String sql = "INSERT INTO `user` (`username`, `password`, `email`, `phone_no`, `address`, `profile_name`, `user_type`)"
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);
            PreparedStatement ps = conn.connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, sha256(password));
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setString(5, alamat);
            ps.setString(6, username);
            ps.setInt(7, 1);
            ps.executeUpdate();
            conn.connection.commit();
            ps.close();

            conn.close();

            return 1;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }
    public int checkEmptyFields(String username, String password, String email, String phoneNumber, String alamat) {
        if (username.equals("") || password.equals("") || email.equals("") || phoneNumber.equals("")
                || alamat.equals("") || username.equals("Enter your Username") || password.equals("Enter your Password") || email.equals("Enter your Email") || phoneNumber.equals("Enter your Phone Number") || alamat.equals("Enter your Address")) {
            return 0;
        }
        return 1;
    }

    public int isNameExist(String username) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `user` WHERE `username`='" + username + "'");
            if (!result.isBeforeFirst()) {
                return 0;
            }
            result.next();
            conn.close();
            return 1;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            System.out.println(ex.getMessage());
            return -99;
        }
    }

    public int checkUserType(User user) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT user_type FROM `user` WHERE `username`='" + user.getUsername() + "'");
            result.next();
            int userType = result.getInt("user_type");
            conn.close();
            return userType;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    public int insertToSingelton(String nama) {
        try {
            UserDataSingleton userDataSingleton = UserDataSingleton.getInstance();
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `user` WHERE `username`='" + nama + "'");
            result.next();
            userDataSingleton.setId(result.getInt("id_user"));
            userDataSingleton.setUsername(result.getString("username"));
            userDataSingleton.setPassword(result.getString("password"));
            userDataSingleton.setProfile_name(result.getString("profile_name"));
            userDataSingleton.setEmail(result.getString("email"));
            userDataSingleton.setPhone_no(result.getString("phone_no"));
            userDataSingleton.setAddress(result.getString("address"));
            userDataSingleton.setUser_type(result.getInt("user_type"));
            conn.close();
            return 1;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -1;
        }
    }

    public void setPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setEchoChar((char) 0); 
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*'); 
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
    }

    // Line Ini Jangan dihapus yak sementara yak biar ga pusing
    public int totalPoinMembership(User user) {
        if (user instanceof MembershipCustomer) {
            MembershipCustomer membershipCustomer = (MembershipCustomer) user;
            return membershipCustomer.getPoin();
        } else {
            return 0;
        }
    }

    public MembershipCustomer registerMembership(String username, String password, String profileName, String email,
            String phoneNumber, String address, int poin) {
        MembershipCustomer membershipCustomer = new MembershipCustomer(username, password, profileName, email,
                phoneNumber, address, null, poin);
        return membershipCustomer;
    }

    // Common services
    public String sha256(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(content.getBytes(StandardCharsets.UTF_8));

            return String.format("%064x", new BigInteger(1, hash));
        } catch (NoSuchAlgorithmException NSAex) {
            new ExceptionLogger(NSAex.getMessage());
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }
        return null;
    }

    public String getLastOpenedDirectory() {
        try {
            Scanner scanner = new Scanner(new File(Config.Path.TEMP_DIR + "lastdir.txt"));
            return scanner.nextLine();
        } catch (FileNotFoundException fileNotFoundEx) {
            new ExceptionLogger(fileNotFoundEx.getMessage());
            return "";
        }
    }

    public int saveLastOpenedDirectory(String path) {
        try {
            File file = new File(Config.Path.TEMP_DIR + "lastdir.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(path);
            fileWriter.close();
            return 0; // Success
        } catch (IOException ioEx) {
            new ExceptionLogger(ioEx.getMessage());
            return -1;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    public void programStart() {
        File dir = new File(Config.Path.TEMP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Hitung pendapatan area
    public int hitungPendapatanCabangFNB(String nama) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT SUM(`harga`) FROM `fnb` WHERE `id_cinema`='" + nama + "'");

            result.next();

            int total = result.getInt(1);

            result.close();
            statement.close();
            conn.close();

            return total;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return 0;
        }
    }

    // Function tampilkan list
    public String[] listKota() {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT DISTINCT `kota` FROM `cinema` ");

            ArrayList<String> listKota = new ArrayList<String>();
            while (result.next()) {
                listKota.add(result.getString("kota"));
            }
            result.close();
            statement.close();
            conn.close();

            return listKota.toArray(new String[listKota.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public String[] listCinema(String kota) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `id_cinema` FROM `cinema` WHERE `kota`='" + kota + "'");

            ArrayList<String> listKota = new ArrayList<String>();
            while (result.next()) {
                listKota.add(result.getString("id_cinema"));
            }
            result.close();
            statement.close();
            conn.close();

            return listKota.toArray(new String[listKota.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public String[] listStudio(String id_cinema) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `id_studio` FROM `studio` WHERE `id_cinema`='" + id_cinema + "'");

            ArrayList<String> listStudio = new ArrayList<String>();
            while (result.next()) {
                listStudio.add(result.getString("id_studio"));
            }
            result.close();
            statement.close();
            conn.close();

            return listStudio.toArray(new String[listStudio.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public String[] listFNB() {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `nama` FROM `fnb`");

            ArrayList<String> listFNB = new ArrayList<String>();
            while (result.next()) {
                listFNB.add(result.getString("nama"));
            }
            result.close();
            statement.close();
            conn.close();

            return listFNB.toArray(new String[listFNB.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public String[] listMovie(String id_Studio) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `id_movie` FROM `jadwal` WHERE `id_studio`='" + id_Studio + "'");

            ArrayList<String> listMovie = new ArrayList<String>();
            while (result.next()) {
                listMovie.add(result.getString("id_movie"));
            }
            result.close();
            statement.close();
            conn.close();

            return listMovie.toArray(new String[listMovie.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public long hargaPerFnb(String namaFnb) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT `harga` FROM `fnb` WHERE `nama` ='" + namaFnb + "'");
            long harga = 0;
            harga = result.getLong("harga");
            result.close();
            statement.close();
            conn.close();
            return harga;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return 0;
        }
    }

    public long totalHasilTransaksiFnb(long harga, int quantity) {
        long total = 0;
        total = harga * quantity;
        return total;
    }

    public String insertTransaksiFnb(String pilihan, int quantity, long harga, String idCinema) {
        try {
            // Execute a SELECT statement to get the current auto-increment value
            conn.open();
            String selectQuery = "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'transaction'";
            PreparedStatement selectStatement = conn.connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            long autoIncrementValue = 0;
            if (resultSet.next()) {
                autoIncrementValue = resultSet.getLong("AUTO_INCREMENT");
            }

            String insertQuery = "INSERT INTO `transaction` (`id_transaction`, `id_user`, `transaction_date`) " +
                    "VALUES (?, ?, NOW())";
            PreparedStatement insertStatement = conn.connection.prepareStatement(insertQuery);
            insertStatement.setString(1, "T-" + String.format("%018d", autoIncrementValue));
            insertStatement.setInt(2, 5);
            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                return "Transaksi berhasil";
            } else {
                return "Transaksi Gagal";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // User Profile area
    public int editProfile(String username, String oldPassword, String newPassword, String profileName, String email,
            String phoneNo, String address) {
        if (username == null) {
            return -1;
        }
        if (oldPassword == null) {
            return -2;
        }
        if (email == null) {
            return -3;
        }
        if (phoneNo == null) {
            return -4;
        }
        if (address == null) {
            return -5;
        }
        try {
            conn.open();
            String selectQuery = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement selectStatement = conn.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, UserDataSingleton.getInstance().getUsername());
            selectStatement.setString(2, sha256(oldPassword));

            ResultSet result = selectStatement.executeQuery();

            if (!result.isBeforeFirst()) {
                return 0;
            }

            result.next();
            int id_user = result.getInt("id_user");

            String updateQuery = "UPDATE user SET username=?, password=?, profile_name=?, email=?, phone_no=?, address=? WHERE id_user=?";
            PreparedStatement updateStatement = conn.connection.prepareStatement(updateQuery);
            updateStatement.setString(1, username);
            updateStatement.setString(2, sha256(newPassword));
            updateStatement.setString(3, profileName);
            updateStatement.setString(4, email);
            updateStatement.setString(5, phoneNo);
            updateStatement.setString(6, address);
            updateStatement.setInt(7, id_user);
            updateStatement.executeUpdate();

        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
            return -99;
        }
        UserDataSingleton.getInstance().setUsername(username);
        UserDataSingleton.getInstance().setPassword(sha256(newPassword));
        UserDataSingleton.getInstance().setProfile_name(profileName);
        UserDataSingleton.getInstance().setEmail(email);
        UserDataSingleton.getInstance().setPhone_no(phoneNo);
        UserDataSingleton.getInstance().setAddress(address);
        return 1;
    }
    // Main menu user area

    public boolean checkMembership(String username){
        try{
            conn.open();
            String selectQuery =  "SELECT `membership_status` FROM user WHERE username='"+username+"' AND membership_status=`1`";
             PreparedStatement preparedStatement = conn.connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery();
            int membershipStatus = resultSet.getInt("membership_status");
            if (membershipStatus==1) {

                return true;
             }
        }catch  (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return false;  
    }

    public void printTable(){
        
    }
    public boolean revokeMembership (){
        return true;
    }   
}
