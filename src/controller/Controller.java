package src.controller;

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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import src.model.Cinema;
import src.model.movie.Movie;
import src.model.movie.MovieLanguageInterface;
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

    public Controller() { }

    //Seat area
    public int getLastSeatId() {
        int lastId = -1;
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT `id_seat` FROM `seat` ORDER BY `id_seat` DESC LIMIT 1;"
            );

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
            case REGULER: seats = new Seat[15][9]; break;
            case LUXE: seats = new Seat[8][8]; break;
            case JUNIOR: seats = new Seat[10][9]; break;
            case VIP: seats = new Seat[5][5]; break;
        }

        int idSeat = getLastSeatId() + 1;

        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = new Seat(
                    String.valueOf(idSeat),
                    ((char)(i + 65)) + ((j + 1) < 10 ? "0" + String.valueOf(j + 1) : String.valueOf(j + 1)),
                    SeatStatusInterface.AVAILABLE
                );
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

    // Studio area
    public boolean isStudioExists(String idStudio) {
        return isStudioExists(idStudio, false);
    }

    private boolean isStudioExists(String idStudio, boolean includeDeleted) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            String sql = "SELECT * FROM `studio` WHERE `id_studio`='" + idStudio + "'";
            if (!includeDeleted) {
                sql += " AND `is_deleted`=0";
            }
            ResultSet result = statement.executeQuery(sql);

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

    public Studio getStudioById(String idStudio) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM `studio` WHERE `id_studio`='" + idStudio + "' AND `is_deleted`=0;"
            );

            result.next();

            Studio studio = new Studio(
                idStudio,
                result.getString("id_cinema"),
                getStudioClassEnum(result.getString("studio_class")),
                result.getInt("studio_type")
            );

            result.close();
            statement.close();
            conn.close();

            return studio;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Studio> getStudio(String idCinema) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `studio` WHERE `id_cinema`='" + idCinema + "' AND `is_deleted`=0");

            if (!result.isBeforeFirst()) {
                return null;
            }

            ArrayList<Studio> studioList = new ArrayList<Studio>();
            while (result.next()) {
                Studio studio = new Studio(
                    result.getString("id_studio"),
                    result.getString("id_cinema"),
                    getStudioClassEnum(result.getString("studio_class")),
                    result.getInt("studio_type")
                );
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

    public String[] getListStudioClass() {
        return new String[] { "REGULAR", "LUXE", "JUNIOR", "VIP" };
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
            case "2D": return StudioTypeInterface.TYPE2D;
            case "3D": return StudioTypeInterface.TYPE3D;
            case "4D": return StudioTypeInterface.TYPE4D;
            case "5D": return StudioTypeInterface.TYPE5D;
        }

        return -1;
    }

    public String getStudioTypeString(int studioType) {
        switch (studioType) {
            case StudioTypeInterface.TYPE2D: return "2D";
            case StudioTypeInterface.TYPE3D: return "3D";
            case StudioTypeInterface.TYPE4D: return "4D";
            case StudioTypeInterface.TYPE5D: return "5D";
        }

        return "";
    }

    public int addNewStudio(String idStudio, String idCinema, String studioClass, String studioType) {
        if (idStudio == null || idStudio.equals("")) {
            return -1;
        }
        if (idStudio.length() != 10) {
            return -2;
        }
        if (isStudioExists(idStudio, true)) {
            return -3;
        }
        if (idCinema == null || idCinema.equals("")) {
            return -4;
        }
        if (idCinema.length() != 10) {
            return -5;
        }
        if (studioClass == null || studioClass.equals("")) {
            return -6;
        }
        if (studioType == null || studioType.equals("")) {
            return -7;
        }

        return addNewStudio(
            idStudio,
            idCinema, 
            getStudioClassEnum(studioClass),
            getStudioType(studioType)
        );
    }

    private int addNewStudio(String idStudio, String idCinema, StudioClassEnum studioClass, int studioType) {
        try {
            conn.open();

            String sql = "INSERT INTO `studio` (`id_studio`, `id_cinema`, `studio_class`, `studio_type`, `is_deleted`)" +
                    "VALUES (?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            PreparedStatement ps = conn.connection.prepareStatement(sql);

            ps.setString(1, idStudio);
            ps.setString(2, idCinema);
            ps.setString(3, getStudioClassString(studioClass).toUpperCase());
            ps.setInt(4, studioType);
            ps.setInt(5, 0);
            
            ps.executeUpdate();
            conn.connection.commit();
            ps.close();

            conn.close();

            // Generate seats on studio creation
            if (generateSeat(new Studio(idStudio, idCinema, studioClass, studioType)) != 0) {
                return -8;
            }

            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    public int editStudio(String idStudio, String idCinema, String studioClass, String studioType) {
        if (idStudio == null || idStudio.equals("")) {
            return -1;
        }

        if (idCinema == null || idCinema.equals("")) {
            return -2;
        }

        if (studioClass == null || studioClass.equals("")) {
            return -3;
        }

        if (studioType == null || studioType.equals("")) {
            return -4;
        }

        return editStudio(
            idStudio,
            idCinema, 
            getStudioClassEnum(studioClass),
            getStudioType(studioType)
        );
    }

    private int editStudio(String idStudio, String idCinema, StudioClassEnum studioClass, int studioType) {
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
                    "SELECT `nama` FROM `cinema` AND `is_deleted`=0;");

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
        return isCinemaExists(idCinema, false);
    }

    private boolean isCinemaExists(String idCinema, boolean includeDeleted) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();

            String sql = "SELECT * FROM `cinema` WHERE `id_cinema`='" + idCinema + "'";
            if (!includeDeleted) {
                sql += " AND `is_deleted`=0";
            }

            ResultSet result = statement.executeQuery(sql);
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

    public Cinema getCinemaById(String idCinema) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `cinema` WHERE `id_cinema`='" + idCinema + "' AND `is_deleted`=0;");

            result.next();

            File fotoCinema = new File(Config.Path.TEMP_DIR + "img.png");
            fotoCinema.createNewFile();

            Path target = fotoCinema.toPath();
            Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

            fotoCinema = new File(Config.Path.TEMP_DIR + "img.png");

            Cinema cinema = null;
            cinema = new Cinema(
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
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public int addNewCinema(String idCinema, String nama, String alamat, String kota, File fotoCinema) {
        if (idCinema == null || idCinema.equals("")) {
            return -1;
        }
        if (idCinema.length() != 10) {
            return -2;
        }
        if (nama == null || nama.equals("")) {
            return -3;
        }
        if (alamat == null || alamat.equals("")) {
            return -4;
        }
        if (kota == null || kota.equals("")) {
            return -5;
        }
        if (fotoCinema == null) {
            return -6;
        }
        if (isCinemaExists(idCinema, true)) {
            return -7;
        }

        try {
            conn.open();

            String sql = "INSERT INTO `cinema` (`id_cinema`, `nama`, `kota`, `alamat`, `img`, `is_deleted`)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            try (
                FileInputStream fis = new FileInputStream(fotoCinema);
                PreparedStatement ps = conn.connection.prepareStatement(sql);
            ) {
                ps.setString(1, idCinema);
                ps.setString(2, nama);
                ps.setString(3, kota);
                ps.setString(4, alamat);
                ps.setBinaryStream(5, fis, (int) fotoCinema.length());
                ps.setInt(6, 0);

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
            conn.connection.setAutoCommit(false);

            String sql = "UPDATE `cinema` SET `nama`=?, `alamat`=?, `kota`=?, `img`=? WHERE `id_cinema`=?;";

            try (
                FileInputStream fis = new FileInputStream(fotoCinema);
                PreparedStatement ps = conn.connection.prepareStatement(sql);
            ) {
                ps.setString(1, nama);
                ps.setString(2, alamat);
                ps.setString(3, kota);
                ps.setBinaryStream(4, fis, (int) fotoCinema.length());
                ps.setString(5, idCinema);

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

    public int deleteCinema(String idCinema) {
        if (idCinema == null || idCinema.equals("")) {
            return -1;
        }

        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            
            statement.executeUpdate(
                "UPDATE `cinema` SET `is_deleted`=1 WHERE `id_cinema`='" + idCinema + "';"
            );

            statement.close();
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
                    "SELECT * FROM `movie` WHERE `id_movie`='" + idMovie + "' AND `is_deleted`=0");

            result.next();

            File fotoMovie = new File(Config.Path.TEMP_DIR + "img.png");
            fotoMovie.createNewFile();

            Path target = fotoMovie.toPath();
            Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

            fotoMovie = new File(Config.Path.TEMP_DIR + "img.png");

            Movie movie = new Movie(
                    idMovie,
                    result.getString("judul"),
                    dateToLocalDate(result.getDate("release_date")),
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
        return isMovieExists(idMovie, false);
    }

    public boolean isMovieExists(String idMovie, boolean includeDeleted) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            String sql =  "SELECT * FROM `movie` WHERE `id_movie`='" + idMovie + "'";

            if (!includeDeleted) {
                sql += " AND `is_deleted`=0";
            }
            ResultSet result = statement.executeQuery(sql);

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
    
    public int addNewMovie(String idMovie, String judul, String releaseDate, String director, String language, String durasi, String sinopsis, File fotoMovie) {
        if (idMovie == null || idMovie.equals("")) {
            return -1;
        }
        if (judul == null || judul.equals("")) {
            return -2;
        }
        if (releaseDate == null || releaseDate.equals("")) {
            return -3;
        }
        if (director == null || director.equals("")) {
            return -4;
        }
        if (language == null || language.equals("")) {
            return -5;
        }
        if (durasi == null || durasi.equals("")) {
            return -6;
        }
        if (!isNumber(durasi)) {
            return -7;
        }
        if (sinopsis == null || sinopsis.equals("")) {
            return -8;
        }
        if (fotoMovie == null) {
            return -9;
        }

        if (isMovieExists(idMovie, true)) {
            return -10;
        }

        return addNewMovie(
            idMovie,
            judul,
            stringToLocalDate(releaseDate, "MMM d, yyyy"),
            director,
            getMovieLanguage(language),
            Integer.parseInt(durasi),
            sinopsis,
            fotoMovie
        );
    }

    private int addNewMovie(String idMovie, String judul, LocalDate releaseDate, String director, int language, int durasi, String sinopsis, File fotoMovie) {
        try {
            conn.open();

            String sql = "INSERT INTO `movie` (`id_movie`, `judul`, `release_date`, `director`, `language`, `durasi`, `sinopsis`, `img`, `is_deleted`)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            try (
                FileInputStream fis = new FileInputStream(fotoMovie);
                PreparedStatement ps = conn.connection.prepareStatement(sql);
            ) {
                ps.setString(1, idMovie);
                ps.setString(2, judul);
                ps.setDate(3, java.sql.Date.valueOf(releaseDate));
                ps.setString(4, director);
                ps.setInt(5, language);
                ps.setInt(6, durasi);
                ps.setString(7, sinopsis);
                ps.setBinaryStream(8, fis, (int) fotoMovie.length());
                ps.setInt(9, 0);
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

    public int editMovie(String idMovie, String judul, String releaseDate, String director, String language, String durasi, String sinopsis, File fotoMovie) {
        if (idMovie == null || idMovie.equals("")) {
            return -1;
        }
        if (judul == null || judul.equals("")) {
            return -2;
        }
        if (releaseDate == null || releaseDate.equals("")) {
            return -3;
        }
        if (director == null || director.equals("")) {
            return -4;
        }
        if (language == null || language.equals("")) {
            return -5;
        }
        if (durasi == null || durasi.equals("")) {
            return -6;
        }
        if (!isNumber(durasi)) {
            return -7;
        }
        if (sinopsis == null || sinopsis.equals("")) {
            return -8;
        }
        if (fotoMovie == null) {
            return -9;
        }
        
        return editMovie(
            idMovie,
            judul,
            stringToLocalDate(releaseDate, "MMM d, yyyy"),
            director,
            getMovieLanguage(language),
            Integer.parseInt(durasi),
            sinopsis,
            fotoMovie
        );
    }
    
    private int editMovie(String idMovie, String judul, LocalDate releaseDate, String director, int language, int durasi, String sinopsis, File fotoMovie) {
        String sql = "UPDATE `movie` SET `judul`=?, `release_date`=?, `director`=?, `language`=?, `durasi`=?, `sinopsis`=?, `img`=?, `is_deleted`=0 " +
            "WHERE `id_movie`=?";

        try {
            conn.open();

            conn.connection.setAutoCommit(false);

            try (
                FileInputStream fis = new FileInputStream(fotoMovie);
                PreparedStatement ps = conn.connection.prepareStatement(sql);
            ) {
                ps.setString(1, judul);
                ps.setDate(2, java.sql.Date.valueOf(releaseDate));
                ps.setString(3, director);
                ps.setInt(4, language);
                ps.setInt(5, durasi);
                ps.setString(6, sinopsis);
                ps.setBinaryStream(7, fis, (int) fotoMovie.length());
                ps.setString(8, idMovie);

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

    public int deleteMovie(String idMovie) {
        if (idMovie == null || idMovie.equals("")) {
            return -1;
        }

        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            
            statement.executeUpdate(
                "UPDATE `movie` SET `is_deleted`=1 WHERE `id_movie`='" + idMovie + "';"
            );

            statement.close();
            conn.close();

            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }

    public String[] getMovieLanguageList() {
        return new String[] { "English", "Japanese", "Bahasa_Indonesia" };
    }

    public String getMovieLanguageString(int language) {
        switch (language) {
            case MovieLanguageInterface.ENGLISH: return "English";
            case MovieLanguageInterface.JAPANESE: return "Japanese";
            case MovieLanguageInterface.BAHASA_INDONESIA: return "Bahasa_Indonesia";
        }
        return "";
    }

    public int getMovieLanguage(String language) {
        switch (language) {
            case "English": return MovieLanguageInterface.ENGLISH;
            case "Japanese": return MovieLanguageInterface.JAPANESE;
            case "Bahasa_Indonesia": return MovieLanguageInterface.BAHASA_INDONESIA;
        }

        return -1;
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
                            result.getString("phoneNumber"),
                            result.getString("address"), null);
                    break;

                case 2:
                    user = new MembershipCustomer(
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("profile_name"),
                            result.getString("email"),
                            result.getString("phoneNumber"),
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
                    "SELECT `id_user` FROM `user` WHERE `username`='" + username + "' AND `password`='"
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
        if (username == null || username.equals("")) {
            return -1;
        }
        if (password == null || password.equals("")) {
            return -2;
        }
        if (email == null || email.equals("")) {
            return -3;
        }
        if (phoneNumber == null || phoneNumber.equals("")) {
            return -4;
        }
        if (alamat == null || alamat.equals("")) {
            return -5;
        }

        try {
            int isNameExist = isNameExist(username);
            if (isNameExist == 1) {
                return 0;
            }
            
            conn.open();
            String sql = "INSERT INTO `user` (`username`, `password`, `email`, `phone_no`, `address`, `profile_name`, `user_type`)" +
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

    public int isNameExist(String username) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM `user` WHERE `username`='" + username + "'"
            );
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

    //Line Ini Jangan dihapus yak sementara yak biar ga pusing
    public int totalPoinMembership(User user) {
        if (user instanceof MembershipCustomer) {
            MembershipCustomer membershipCustomer = (MembershipCustomer) user;
            return membershipCustomer.getPoin();
        } else {
            return 0; 
        }
    }

    public MembershipCustomer registerMembership(String username, String password, String profileName, String email, String phoneNumber, String address, int poin) {
        MembershipCustomer membershipCustomer = new MembershipCustomer(username, password, profileName, email, phoneNumber, address, null, poin);
        return membershipCustomer;
    }

    // Common services
    public boolean isNumber(String string) {
        for (int i = 0; i < string.length(); i++) {
            int chr = (char) string.charAt(i);

            if (i == 0 && chr == 45) {
                continue;
            }

            if (!(chr >= 48 && chr <= 57)) {
                return false;
            }
        }

        return true;
    }

    public LocalDate dateToLocalDate(java.util.Date date) {
        return LocalDate.from(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()));
    }

    public LocalDate stringToLocalDate(String str, String pattern) {
        if (str.equals("")) { return null; }
        LocalDate output = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            output = LocalDate.parse(str, formatter);
        } catch (Exception ex) {
            return null;
        }

        return output;
    }

    public String localDateToString(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
    
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
    //Hitung pendapatan area
    public int hitungPendapatanCabangFNB(String nama){
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
    //Function tampilkan list
    public String[] listKota(){
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

    public String[] listCinema(String kota){
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
    public String[] listStudio(String id_cinema){
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
    public String[] listFNB(){
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
    public String[] listMovie(String id_Studio){
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
}
