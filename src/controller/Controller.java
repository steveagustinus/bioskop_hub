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
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.model.Cinema;
import src.model.FnB;
import src.model.Jadwal;
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
import src.view.MainInterface;

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
                seats = new Seat[9][15];
                break;
            case LUXE:
                seats = new Seat[8][8];
                break;
            case JUNIOR:
                seats = new Seat[9][10];
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

    public Seat[][] getSeatFromJadwal(String idJadwal) {
        Seat[][] seats = new Seat[1][1];

        String idStudio = idJadwal.substring(3, 13);

        Studio studio = getStudioById(idStudio);
        switch (studio.getStudioClass()) {
            case REGULER:
                seats = new Seat[9][15];
                break;
            case LUXE:
                seats = new Seat[8][8];
                break;
            case JUNIOR:
                seats = new Seat[9][10];
                break;
            case VIP:
                seats = new Seat[5][5];
                break;
        }

        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `id_seat`, `kode` FROM `seat` WHERE `id_studio`='" + studio.getIdStudio() + "'");

            if (!result.isBeforeFirst()) {
                return null;
            }

            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    result.next();
                    seats[i][j] = new Seat(
                            result.getString("id_seat"),
                            result.getString("kode"),
                            SeatStatusInterface.AVAILABLE);
                }
            }

            conn.close();

            statement = conn.connection.createStatement();
            result = statement.executeQuery(
                    "SELECT `id_seat` FROM `transaction_jadwal` WHERE `id_jadwal`='" + idJadwal +
                            "' ORDER BY `id_seat` ASC;");

            if (!result.isBeforeFirst()) {
                return seats;
            }

            result.next();

            mainloop: for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    if (seats[i][j].getIdSeat().equals(result.getString("id_seat"))) {
                        seats[i][j].setSeatStatus(SeatStatusInterface.TAKEN);
                        if (result.isLast()) {
                            break mainloop;
                        } else {
                            result.next();
                        }
                    }
                }
            }

            result.close();
            conn.close();

            return seats;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }

        return null;
    }

    public Seat[][] getSeatFromJadwal(Jadwal[] arrJadwal, String idJadwal) {
        for (Jadwal jadwal : arrJadwal) {
            if (jadwal.getIdJadwal().equals(idJadwal)) {
                return jadwal.getSeat();
            }
        }

        return null;
    }

    public Seat[] getSeatFromListSeatString(Seat[][] seats, ArrayList<String> seatIds) {
        Seat[] output = new Seat[seatIds.size()];

        main: for (int i = 0; i < seatIds.size(); i++) {
            for (Seat[] arrSeat : seats) {
                for (Seat seat : arrSeat) {
                    if (seat.getSeatCode().equals(seatIds.get(i))) {
                        output[i] = seat;
                        continue main;
                    }
                }
            }
        }

        return output;
    }

    // Jadwal area
    public Jadwal getJadwalById(String idJadwal) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `jadwal` WHERE `id_jadwal`='" + idJadwal + "';");

            result.next();

            Jadwal jadwal = new Jadwal(
                    result.getString("id_jadwal"),
                    result.getString("id_movie"),
                    result.getString("id_studio"),
                    result.getInt("harga"),
                    result.getTimestamp("waktu").toLocalDateTime(),
                    getSeatFromJadwal(result.getString("id_jadwal")));

            result.close();
            statement.close();
            conn.close();

            return jadwal;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public String generateJadwalID(String idStudio, String idMovie, String tanggal, String jam) {
        if (idStudio == null || idStudio.equals("")) {
            return "";
        }
        if (idMovie == null || idMovie.equals("")) {
            return "";
        }

        LocalDateTime tanggalWaktu = null;
        try {
            tanggalWaktu = LocalDateTime.parse(tanggal + " " + jam, DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
        } catch (DateTimeParseException ex) {
            return "";
        }

        String output = "SH_" + idStudio + "_";

        String movieString = idMovie;
        for (int i = idMovie.length(); i < 10; i++) {
            movieString += "X";
        }

        output += movieString + "_";

        output += tanggalWaktu.format(DateTimeFormatter.ofPattern("yyMMddHHmm"));

        return output;
    }

    public int addNewJadwal(String idJadwal, String idStudio, String idMovie, String harga, String tanggal,
            String jam) {
        if (idJadwal == null || idJadwal.equals("")) {
            return -1;
        }
        if (idJadwal.length() != 35) {
            return -2;
        }
        if (idStudio == null || idStudio.equals("")) {
            return -3;
        }
        if (!isStudioExists(idStudio)) {
            return -4;
        }
        if (idMovie == null || idMovie.equals("")) {
            return -5;
        }
        if (!isMovieExists(idMovie)) {
            return -6;
        }
        if (harga == null || harga.equals("")) {
            return -7;
        }
        if (!isNumber(harga)) {
            return -8;
        }
        if (tanggal == null || tanggal.equals("")) {
            return -9;
        }
        if (jam == null || jam.equals("")) {
            return -10;
        }

        LocalDateTime tanggalWaktu = null;
        try {
            tanggalWaktu = LocalDateTime.parse(tanggal + " " + jam, DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
        } catch (DateTimeParseException ex) {
            return -11;
        }

        return addNewJadwal(
                idJadwal,
                idStudio,
                idMovie,
                Integer.parseInt(harga),
                tanggalWaktu);
    }

    private int addNewJadwal(String idJadwal, String idStudio, String idMovie, int harga, LocalDateTime waktu) {
        try {
            conn.open();

            String sql = "INSERT INTO `jadwal` (`id_jadwal`, `id_studio`, `id_movie`, `waktu`, `harga`)" +
                    "VALUES (?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            PreparedStatement ps = conn.connection.prepareStatement(sql);

            ps.setString(1, idJadwal);
            ps.setString(2, idStudio);
            ps.setString(3, idMovie);
            ps.setString(4, waktu.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setInt(5, harga);

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

    public Jadwal[] getJadwalByTimeRange(String idCinema, LocalDate start, LocalDate end) {
        String startDate = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        String endDate = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";

        try {
            conn.open();

            String sql = "SELECT * FROM jadwal " +
                    "WHERE id_studio IN (" +
                    "SELECT id_studio FROM studio " +
                    "WHERE id_cinema = '" + idCinema + "'" +
                    ") " +
                    "AND waktu > '" + startDate + "' " +
                    "AND waktu < '" + endDate + "' " +
                    "ORDER BY waktu ASC;";

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            ArrayList<Jadwal> listJadwal = new ArrayList<Jadwal>();

            while (result.next()) {
                listJadwal.add(
                        new Jadwal(
                                result.getString("id_jadwal"),
                                result.getString("id_movie"),
                                result.getString("id_studio"),
                                result.getInt("harga"),
                                result.getTimestamp("waktu").toLocalDateTime(),
                                getSeatFromJadwal(result.getString("id_jadwal"))));
            }

            if (listJadwal.size() == 0) {
                return null;
            }
            return listJadwal.toArray(new Jadwal[listJadwal.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public Jadwal[] filterJadwal(Jadwal[] listJadwal, String idMovie) {
        ArrayList<Jadwal> output = new ArrayList<Jadwal>();

        for (int i = 0; i < listJadwal.length; i++) {
            if (listJadwal[i].getIdMovie().equals(idMovie)) {
                output.add(listJadwal[i]);
            }
        }

        return output.toArray(new Jadwal[output.size()]);
    }

    public String[][] getJadwalData(Jadwal[] listJadwal) {
        String[][] output = new String[listJadwal.length][5];

        for (int i = 0; i < listJadwal.length; i++) {
            // output[i][0] =
            // listJadwal[i].getWaktu().format(DateTimeFormatter.ofPattern("dd MMMM
            // hh:mm"));
            // output[i][1] = getMovieById(listJadwal[i].getIdMovie()).getJudul();
            // Studio studio = getStudioById(listJadwal[i].getIdStudio());
            // output[i][2] = getStudioClassString(studio.getStudioClass());
            // output[i][3] = getStudioTypeString(studio.getStudioType());
            // output[i][4] = String.valueOf(listJadwal[i].getHarga());

            String item = listJadwal[i].getWaktu().format(DateTimeFormatter.ofPattern("dd MMMM hh:mm"));
            item += " | " + getMovieById(listJadwal[i].getIdMovie()).getJudul();
            Studio studio = getStudioById(listJadwal[i].getIdStudio());
            item += " | " + getStudioClassString(studio.getStudioClass());
            item += " | " + getStudioTypeString(studio.getStudioType());
            item += " | " + String.valueOf(listJadwal[i].getHarga());

            output[i][0] = item;
        }

        return output;
    }

    public boolean deleteJadwal (String idJadwal){
        try{
            conn.open();
            String deleteQuery = "DELETE FROM `jadwal` WHERE `id_jadwal` = ? AND NOT EXISTS (SELECT `id_jadwal` FROM `transaction_jadwal` WHERE `id_jadwal` = ?)";
            try (PreparedStatement preparedStatement = conn.connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, idJadwal);
                preparedStatement.setString(2, idJadwal);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex){
            new ExceptionLogger(ex.getMessage());
            return false;
        }
    }

    public Movie[] extractMoviesFromListJadwal(Jadwal[] arrJadwal) {
        if (arrJadwal == null) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<Movie>();

        ArrayList<String> idsMovie = new ArrayList<String>();
        for (Jadwal jadwal : arrJadwal) {
            if (!idsMovie.contains(jadwal.getIdMovie())) {
                idsMovie.add(jadwal.getIdMovie());
            }
        }
        Movie[] arrMovie = getMovies();
        for (String idMovie : idsMovie) {
            for (int i = 0; i < arrMovie.length; i++) {
                if (arrMovie[i].getIdMovie().equals(idMovie)) {
                    movies.add(arrMovie[i]);
                    break;
                }
            }
        }

        return movies.toArray(new Movie[movies.size()]);
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
                    "SELECT * FROM `studio` WHERE `id_studio`='" + idStudio + "' AND `is_deleted`=0;");

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
                getStudioType(studioType));
    }

    private int addNewStudio(String idStudio, String idCinema, StudioClassEnum studioClass, int studioType) {
        try {
            conn.open();

            String sql = "INSERT INTO `studio` (`id_studio`, `id_cinema`, `studio_class`, `studio_type`, `is_deleted`)"
                    +
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

    public int editStudio(String idStudio, String studioClass, String studioType) {
        if (idStudio == null || idStudio.equals("")) {
            return -1;
        }

        if (studioClass == null || studioClass.equals("")) {
            return -2;
        }

        if (studioType == null || studioType.equals("")) {
            return -3;
        }

        return editStudio(
                idStudio,
                getStudioClassEnum(studioClass),
                getStudioType(studioType));
    }

    private int editStudio(String idStudio, StudioClassEnum studioClass, int studioType) {
        try {
            conn.open();

            String sql = "UPDATE `studio` SET `studio_class`=?, `studio_type`=? WHERE `id_studio`=?;";
            PreparedStatement ps = conn.connection.prepareStatement(sql);

            ps.setString(1, getStudioClassString(studioClass).toUpperCase());
            ps.setInt(2, studioType);
            ps.setString(3, idStudio);

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

    public int deleteStudio(String idStudio) {
        if (idStudio == null || idStudio.equals("")) {
            return -1;
        }

        try {
            conn.open();
            Statement statement = conn.connection.createStatement();

            statement.executeUpdate(
                    "UPDATE `studio` SET `is_deleted`=1 WHERE `id_studio`='" + idStudio + "';");

            statement.close();
            conn.close();
            return 0;
        } catch (Exception ex) {
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
                    null);

            result.close();
            statement.close();
            conn.close();

            return cinema;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public Cinema[] getCinemas(String kota) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM `cinema` WHERE `kota`='" + kota + "' AND `is_deleted`=0;"
            );

            if (!result.isBeforeFirst()) {
                return null;
            }

            ArrayList<Cinema> cinemaList = new ArrayList<Cinema>();

            int cinemaCounter = 0;
            while(result.next()) {

                File fotoCinema = new File(Config.Path.TEMP_DIR + "img_cinema_" + cinemaCounter + ".png");
                fotoCinema.createNewFile();

                Path target = fotoCinema.toPath();
                Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

                fotoCinema = new File(Config.Path.TEMP_DIR + "img_cinema_" + cinemaCounter + ".png");

                Cinema cinema = new Cinema(
                    result.getString("id_cinema"),
                    result.getString("nama"),
                    result.getString("alamat"),
                    result.getString("kota"),
                    fotoCinema,
                    null
                );

                cinemaList.add(cinema);
                cinemaCounter++;
            }

            result.close();
            statement.close();
            conn.close();

            return cinemaList.toArray(new Cinema[cinemaList.size()]);
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
                    PreparedStatement ps = conn.connection.prepareStatement(sql);) {
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
                    PreparedStatement ps = conn.connection.prepareStatement(sql);) {
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
                    "UPDATE `cinema` SET `is_deleted`=1 WHERE `id_cinema`='" + idCinema + "';");

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

    public Movie[] getMovies() {
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `movie` WHERE `is_deleted`=0");

            int counter = 0;
            while (result.next()) {
                File fotoMovie = new File(Config.Path.TEMP_DIR + "img_" + counter + ".png");
                fotoMovie.createNewFile();

                Path target = fotoMovie.toPath();
                Files.copy(result.getBinaryStream("img"), target, StandardCopyOption.REPLACE_EXISTING);

                fotoMovie = new File(Config.Path.TEMP_DIR + "img_" + counter + ".png");

                Movie movie = new Movie(
                        result.getString("id_movie"),
                        result.getString("judul"),
                        dateToLocalDate(result.getDate("release_date")),
                        result.getString("director"),
                        result.getInt("language"),
                        result.getInt("durasi"),
                        result.getString("sinopsis"),
                        fotoMovie);

                movieList.add(movie);
                counter++;
            }

            result.close();
            statement.close();
            conn.close();

            return movieList.toArray(new Movie[movieList.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public Movie[] searchMovie(String input, int limit) {
        Movie[] movies = getMovies();
        if (movies == null) { return null; }

        ArrayList<Movie> movieList = new ArrayList<Movie>();

        if (input.equals("")) {
            for (int i = 0; i < limit && i < movies.length; i++) {
                movieList.add(movies[i]);
            }

            return movieList.toArray(new Movie[movieList.size()]);
        }

        for (Movie movie : movies) {
            if (movie.getJudul().toLowerCase().contains(input.toLowerCase())) {
                movieList.add(movie);
            }
        }

        if (movieList.isEmpty()) {
            return null;
        }

        return movieList.toArray(new Movie[movieList.size()]);
    }

    public boolean isMovieExists(String idMovie) {
        return isMovieExists(idMovie, false);
    }

    private boolean isMovieExists(String idMovie, boolean includeDeleted) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            String sql = "SELECT * FROM `movie` WHERE `id_movie`='" + idMovie + "'";

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

    public int addNewMovie(String idMovie, String judul, String releaseDate, String director, String language,
            String durasi, String sinopsis, File fotoMovie) {
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
                fotoMovie);
    }

    private int addNewMovie(String idMovie, String judul, LocalDate releaseDate, String director, int language,
            int durasi, String sinopsis, File fotoMovie) {
        try {
            conn.open();

            String sql = "INSERT INTO `movie` (`id_movie`, `judul`, `release_date`, `director`, `language`, `durasi`, `sinopsis`, `img`, `is_deleted`)"
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);

            try (
                    FileInputStream fis = new FileInputStream(fotoMovie);
                    PreparedStatement ps = conn.connection.prepareStatement(sql);) {
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

    public int editMovie(String idMovie, String judul, String releaseDate, String director, String language,
            String durasi, String sinopsis, File fotoMovie) {
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
                fotoMovie);
    }

    private int editMovie(String idMovie, String judul, LocalDate releaseDate, String director, int language,
            int durasi, String sinopsis, File fotoMovie) {
        String sql = "UPDATE `movie` SET `judul`=?, `release_date`=?, `director`=?, `language`=?, `durasi`=?, `sinopsis`=?, `img`=?, `is_deleted`=0 "
                +
                "WHERE `id_movie`=?";

        try {
            conn.open();

            conn.connection.setAutoCommit(false);

            try (
                    FileInputStream fis = new FileInputStream(fotoMovie);
                    PreparedStatement ps = conn.connection.prepareStatement(sql);) {
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
                    "UPDATE `movie` SET `is_deleted`=1 WHERE `id_movie`='" + idMovie + "';");

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
            case MovieLanguageInterface.ENGLISH:
                return "English";
            case MovieLanguageInterface.JAPANESE:
                return "Japanese";
            case MovieLanguageInterface.BAHASA_INDONESIA:
                return "Bahasa_Indonesia";
        }
        return "";
    }

    public int getMovieLanguage(String language) {
        switch (language) {
            case "English":
                return MovieLanguageInterface.ENGLISH;
            case "Japanese":
                return MovieLanguageInterface.JAPANESE;
            case "Bahasa_Indonesia":
                return MovieLanguageInterface.BAHASA_INDONESIA;
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
                            result.getString("id_user"),
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("profile_name"),
                            result.getString("email"),
                            result.getString("phone_no"),
                            result.getString("address"),
                            null);
                    break;

                case 2:
                    user = new MembershipCustomer(
                            result.getString("id_user"),
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("profile_name"),
                            result.getString("email"),
                            result.getString("phone_no"),
                            result.getString("address"),
                            null,
                            result.getInt("poin"));
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
            String sql = "INSERT INTO `user` (`username`, `password`, `email`, `phone_no`, `address`, `profile_name`, `user_type`, `membership_status`, `membership_expiry_date`, `point_membership`)"
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            conn.connection.setAutoCommit(false);
            PreparedStatement ps = conn.connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, sha256(password));
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setString(5, alamat);
            ps.setString(6, username);
            ps.setInt(7, 1);
            ps.setInt(8, 0);
            ps.setDate(9, null);
            ps.setInt(10, 0);
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
                || alamat.equals("") || username.equals("Enter your Username") || password.equals("Enter your Password")
                || email.equals("Enter your Email") || phoneNumber.equals("Enter your Phone Number")
                || alamat.equals("Enter your Address")) {
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

    // User action
    public int getTotalBayar(Jadwal jadwal, Seat[] bookedSeat) {
        return jadwal.getHarga() * bookedSeat.length;
    }

    public int pesanTiket(String idCustomer, Jadwal jadwal, Seat[] bookedSeat, String paymentMethod) {
        if (idCustomer == null || idCustomer.equals("")) {
            return -1;
        }

        if (jadwal == null) {
            return -2;
        }

        if (bookedSeat == null) {
            return -3;
        }

        if (paymentMethod == null || paymentMethod.equals("")) {
            return -4;
        }

        try {
            String idTransaction = createTransactionId();
            conn.open();

            Statement statement = conn.connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO `transaction` (`id_transaction`, `id_user`, `transaction_date`, `payment_method`) " +
                            "VALUES ('" + idTransaction + "', '" + idCustomer + "', now(), '" + paymentMethod + "');");

            String sql = "INSERT INTO `transaction_jadwal` (`id_transaction`, `id_jadwal`, `id_seat`) VALUES ";

            for (Seat seat : bookedSeat) {
                sql += "('" + idTransaction + "', '" + jadwal.getIdJadwal() + "', '" + seat.getIdSeat() + "'),";
            }

            sql = sql.substring(0, sql.length() - 1) + ";";

            statement.executeUpdate(sql);
            statement.close();
            conn.close();

            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
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

    public String[] getPaymentMethods() {
        return new String[] { "BCA", "GO-PAY", "DANA", "SHOPEEPAY" };
    }

    // Transaction
    public String createTransactionId() {
        String newId = "";
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `id_transaction` FROM `transaction` ORDER BY `id_transaction` DESC LIMIT 1;");

            String lastId = "";
            if (!result.isBeforeFirst()) {
                lastId = "0";
            } else {
                result.next();
                lastId = result.getString("id_transaction").replace("T-", "");
            }

            result.close();
            statement.close();
            conn.close();

            newId = String.valueOf(Integer.parseInt(lastId) + 1);

            for (int i = newId.length(); i < 18; i++) {
                newId = "0" + newId;
            }

            newId = "T-" + newId;

            return newId;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
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
            userDataSingleton.setMembership_status(result.getInt("membership_status"));
            userDataSingleton.setMembership_expiry_date(result.getDate("membership_expiry_date"));
            userDataSingleton.setMembership_point(result.getInt("point_membership"));
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
                    passwordField.setForeground(MainInterface.TEXT_BACKGROUND);
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
        if (str.equals("")) {
            return null;
        }
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

    // FnB area
    public FnB getFnBbyName(String namaFnB) {
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `fnb` WHERE `nama`='" + namaFnB + "' AND `is_deleted`=0;");

            result.next();

            FnB fnB = null;
            fnB = new FnB(
                    result.getString("nama"),
                    result.getInt("harga"),
                    result.getString("description"));

            result.close();
            statement.close();
            conn.close();

            return fnB;

        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    public String hargaPerFnb(String namaFnb) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT `harga` FROM `fnb` WHERE `nama` ='" + namaFnb + "'");
            if (result.next()) {
                String harga = result.getString("harga");
                result.close();
                statement.close();
                conn.close();
                return harga;
            } else {
                result.close();
                statement.close();
                conn.close();
                return "Not Found";
            }
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return "Error";
        }
    }

    public String formatCurrency(int number) {
        DecimalFormat decFormat = new DecimalFormat("###,###");

        return decFormat.format(number);
    }

    public int totalHasilTransaksiFnb(String harga, String quantity, boolean discount) {
        if (harga.equals("") || quantity.equals("")) {
            return 0;
        }
        if (!isNumber(harga) || !isNumber(quantity)) {
            return 0;
        }

        int total = 0;
        int int_harga = Integer.parseInt(harga);
        int int_quantity = Integer.parseInt(quantity);

        if (int_quantity < 0) {
            return 0;
        }

        total = int_harga * int_quantity;

        if (discount) {
            total = total - 100000;
        }
        return total < 0 ? 0 : total;
    }
    
    public String insertTransaksiFnb(String pilihan, int quantity, String cinema, int id_user, String paymentMethod) {
        try {
            if (paymentMethod == null || paymentMethod.equals("")) { return "Pilih metode pembayaran"; }
            conn.open();
            String currentIdTransaction = createTransactionId();
        
            String insertQuery = "INSERT INTO `transaction` (`id_transaction`, `id_user`, `transaction_date`, `payment_method`) " +
                    "VALUES (?, ?, NOW(), ?)";
            PreparedStatement insertStatement = conn.connection.prepareStatement(insertQuery);
            insertStatement.setString(1, currentIdTransaction);
            insertStatement.setInt(2, id_user);
            insertStatement.setString(3, paymentMethod);
            insertStatement.executeUpdate();

            // Get id_fnb
            String selectIdFnb = "SELECT `id_fnb` FROM `fnb` WHERE `nama` = ?";
            PreparedStatement selectStatement2 = conn.connection.prepareStatement(selectIdFnb);
            selectStatement2.setString(1, pilihan);
            ResultSet resultSet2 = selectStatement2.executeQuery();
            String resultString = "";
            if (resultSet2.next()) {
                resultString = resultSet2.getString("id_fnb");
            }

            String insertQuery2 = "INSERT INTO `transaction_fnb` (`id_transaction`, `id_fnb`, `qty`, `id_cinema`) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement2 = conn.connection.prepareStatement(insertQuery2);
            insertStatement2.setString(1, currentIdTransaction);
            insertStatement2.setString(2, resultString);
            insertStatement2.setInt(3, quantity);
            insertStatement2.setString(4, cinema);
            int rowsAffected2 = insertStatement2.executeUpdate();

            if (rowsAffected2 > 0) {
                return "Transaksi Berhasil";
            } else {
                return "Insert Gagal";
            }
        } catch (Exception e) {
            e.printStackTrace();
                return "Insert Gagal";
        }
    }

    // Membership area
    public boolean checkSufficientPoint(int poinUser, int poinNeeded) {
        return poinUser >= poinNeeded;
    }

    public String increasePoinMembership(String username, int statusMembership, int amountPlusPoin){
        if (statusMembership == 1) {
            try {
                conn.open();
                String selectQuery = "SELECT `point_membership` FROM `user` WHERE `username` = ?";
                PreparedStatement selectStatement = conn.connection.prepareStatement(selectQuery);
                selectStatement.setString(1, username);
                ResultSet resultSet = selectStatement.executeQuery();

                int poin = 0;
                if (resultSet.next()) {
                    poin = resultSet.getInt("point_membership");
                }

                int totalPoin = poin + amountPlusPoin;
    
                String updateQuery = "UPDATE `user` SET `point_membership` = ? WHERE `username` = ?";
                PreparedStatement updateStatement = conn.connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, totalPoin);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();
                UserDataSingleton.getInstance().setMembership_point(totalPoin);

                return "Poin berhasil ditambahkan, poin anda sekarang: " + totalPoin;
            } catch (Exception e) {
                e.printStackTrace();
                return "Gagal menambahkan poin";
            }
        } else {
            return "Anda bukan member, poin tidak ditambahkan";
        }
    }

    public String decreasePoinMembership(String username, int statusMembership, int amountMinusPoin){
        if (statusMembership == 1) {
            try{
                conn.open();
                String selectQuery = "SELECT `point_membership` FROM `user` WHERE `username` = ?";
                PreparedStatement selectStatement = conn.connection.prepareStatement(selectQuery);
                selectStatement.setString(1, username);
                ResultSet resultSet = selectStatement.executeQuery();

                int poin = 0;
                if (resultSet.next()) {
                    poin = resultSet.getInt("point_membership");
                }

                int totalPoin = poin - amountMinusPoin;
    
                String updateQuery = "UPDATE `user` SET `point_membership` = ? WHERE `username` = ?";
                PreparedStatement updateStatement = conn.connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, totalPoin);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();
                UserDataSingleton.getInstance().setMembership_point(totalPoin);

                return "Poin berhasil dikurangi, poin anda sekarang: " + totalPoin;
            } catch (Exception e) {
                e.printStackTrace();
                return "Gagal mengurangi poin";
            }
        } else {
            return "Anda bukan member, poin tidak ditambahkan";
        }
    }

    // Hitung pendapatan area
    public int hitungPendapatanCabang(String nama) {
        int total = 0;
        try {
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT SUM(f.harga * tf.qty) FROM transaction t JOIN transaction_fnb tf ON tf.id_transaction = t.id_transaction JOIN fnb f ON f.id_fnb = tf.id_fnb JOIN cinema c ON tf.id_cinema = c.id_cinema WHERE c.nama = '"
                            + nama + "';");
            result.next();
            total += result.getInt(1);
            result.close();
            result = statement.executeQuery(
                    "SELECT COALESCE(SUM(j.harga), 0) FROM transaction t JOIN transaction_jadwal tj ON tj.id_transaction = t.id_transaction JOIN jadwal j ON j.id_jadwal = tj.id_jadwal JOIN studio s ON s.id_studio = j.id_studio JOIN cinema c ON c.id_cinema = s.id_cinema WHERE c.nama ='"
                            + nama + "';");
            result.next();
            total += result.getInt(1);

            result.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            new ExceptionLogger(e.getMessage());
        }
        return total;
    }

    public int hitungPendapatanKota(String kota) {
        int total = 0;
        try {
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT SUM(f.harga * tf.qty) FROM transaction t JOIN transaction_fnb tf ON tf.id_transaction = t.id_transaction JOIN fnb f ON f.id_fnb = tf.id_fnb JOIN cinema c ON tf.id_cinema = c.id_cinema WHERE c.kota = '"
                            + kota + "';");
            result.next();
            total += result.getInt(1);
            result.close();
            result = statement.executeQuery(
                    "SELECT COALESCE(SUM(j.harga), 0) FROM transaction t JOIN transaction_jadwal tj ON tj.id_transaction = t.id_transaction JOIN jadwal j ON j.id_jadwal = tj.id_jadwal JOIN studio s ON s.id_studio = j.id_studio JOIN cinema c ON c.id_cinema = s.id_cinema WHERE c.kota ='"
                            + kota + "';");
            result.next();
            total += result.getInt(1);

            result.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            new ExceptionLogger(e.getMessage());
        }
        return total;
    }

    public int hitungPendapatanTotal() {
        int total = 0;
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT SUM(f.harga * tf.qty) FROM transaction t JOIN transaction_fnb tf ON tf.id_transaction = t.id_transaction JOIN fnb f ON f.id_fnb = tf.id_fnb;");
            result.next();
            total += result.getInt(1);
            result.close();
            result = statement.executeQuery(
                    "SELECT SUM(j.harga) FROM transaction t JOIN transaction_jadwal tj ON tj.id_transaction = t.id_transaction JOIN jadwal j ON j.id_jadwal = tj.id_jadwal;");
            result.next();
            total += result.getInt(1);
            statement.close();
            conn.close();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }
        return total;
    }

    public int hitungPendapatanPerKotaPerCabang(String namaKota, String namaCabang) {
        int total = 0;
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT SUM(f.harga * tf.qty) FROM transaction t JOIN transaction_fnb tf ON tf.id_transaction = t.id_transaction JOIN fnb f ON f.id_fnb = tf.id_fnb JOIN cinema c ON tf.id_cinema = c.id_cinema WHERE c.kota = '"
                            + namaKota + "' AND c.nama='" + namaCabang + "';");
            result.next();
            total += result.getInt(1);
            result.close();
            result = statement.executeQuery(
                    "SELECT COALESCE(SUM(j.harga), 0) FROM transaction t JOIN transaction_jadwal tj ON tj.id_transaction = t.id_transaction JOIN jadwal j ON j.id_jadwal = tj.id_jadwal JOIN studio s ON s.id_studio = j.id_studio JOIN cinema c ON c.id_cinema = s.id_cinema WHERE c.kota = '"
                            + namaKota + "' AND c.nama='" + namaCabang + "';");
            result.next();
            total += result.getInt(1);

            result.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            new ExceptionLogger(e.getMessage());
        }
        return total;
    }

    public boolean isKotaExists(String kota) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `cinema` WHERE `kota`='" + kota + "'");
            if (!result.isBeforeFirst()) {
                return false;
            }
            result.next();
            conn.close();
            return true;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean isCabangExists(String cabang) {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `cinema` WHERE `nama`='" + cabang + "'");
            if (!result.isBeforeFirst()) {
                return false;
            }
            result.next();
            conn.close();
            return true;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public String[] listKotaHP() {
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
    
    public String[] listCabangHP(String namaKota){
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `nama` FROM `cinema` WHERE `kota` = '" + namaKota + "'");

            ArrayList<String> listCabang = new ArrayList<String>();
            while (result.next()) {
                listCabang.add(result.getString("nama"));
            }
            result.close();
            statement.close();
            conn.close();

            return listCabang.toArray(new String[listCabang.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }

    // Function tampilkan list
    public String[] listKota() {
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT DISTINCT `kota` FROM `cinema`");

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
                    "SELECT `nama` FROM `fnb` WHERE `is_deleted`=0");

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
                    "SELECT `id_movie` FROM `jadwal` WHERE `id_studio`='" + id_Studio + "' WHERE `is_deleted`=0");

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

    public int addFnB(String[] fnb) {
        if (fnb[1].equals("") || !isNumber(fnb[1])) {
            return OperationCode.AddFnB.INVALIDHARGA;
        }
        int harga = Integer.parseInt(fnb[1]);
        if (fnb[0] == null) {
            return OperationCode.AddFnB.EMPTYNAME;
        } else if (fnb[1] == null) {
            return OperationCode.AddFnB.EMPTYHARGA;
        } else if (fnb[2] == null) {
            return OperationCode.AddFnB.EMPTYDESCRIPTION;
        }
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO `fnb` (`nama`,`harga`,`description`,`is_deleted`) VALUES ('" + fnb[0] + "','" + harga
                            + "','" + fnb[2] + "',0)");
            statement.close();
            conn.close();
            return OperationCode.AddFnB.SUCCESS;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return OperationCode.AddFnB.ANYEXCEPTION;
        }
    }
    
    public int deleteFnB(String fnbName) {
        try{
            conn.open();

            Statement statement = conn.connection.createStatement();
            statement.executeUpdate(
                    "UPDATE `fnb` SET `is_deleted`=1 WHERE nama='" + fnbName + "'");
            statement.close();
            conn.close();
            return 0;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return -99;
        }
    }
    
    public int editFnB(String fnbName, String [] dataFnB) {
        if (dataFnB[1].equals("") || !isNumber(dataFnB[1])) {
            return OperationCode.AddFnB.INVALIDHARGA;
        }
        int harga = Integer.parseInt(dataFnB[1]);
        if (dataFnB[0] == null) {
            return OperationCode.EditFnB.EMPTYNAME;
        } else if (dataFnB[1] == null) {
            return OperationCode.EditFnB.EMPTYHARGA;
        } else if (dataFnB[2] == null) {
            return OperationCode.EditFnB.EMPTYDESCRIPTION;
        }
        try {
            conn.open();

            Statement statement = conn.connection.createStatement();
            statement.executeUpdate(
                    "UPDATE `fnb` SET `nama`='" + dataFnB[0] + "', `harga`='" + harga + "', `description`='"
                            + dataFnB[2] + "' WHERE nama='" + fnbName + "'");
            statement.close();
            conn.close();
            return OperationCode.EditFnB.SUCCESS;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return OperationCode.EditFnB.ANYEXCEPTION;
        }
    }

    // Main menu user area
    public void printTableFnB(int id, JTable table, DefaultTableModel model) {
        String[] columns = { "Transaction Date", "City", "Cinema", "Transaction Items", "Quantity", "Total Price", "Payment Method" };
        model.setColumnIdentifiers(columns);
        try {
            String sql = "SELECT t.transaction_date,f.nama, tf.qty,f.harga * tf.qty, c.kota, c.nama, t.payment_method FROM transaction t JOIN transaction_fnb tf ON tf.id_transaction = t.id_transaction JOIN fnb f ON f.id_fnb = tf.id_fnb JOIN cinema c ON tf.id_cinema = c.id_cinema JOIN user u ON u.id_user = t.id_user WHERE u.id_user = "
                    + id + " GROUP BY t.id_transaction, tf.qty, tf.id_fnb;";
            PreparedStatement statement = conn.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String lastDate = "x";
            while (resultSet.next()) {
                String transactionDate = resultSet.getString("t.transaction_date");
                if (!lastDate.equals("x")) {
                    if (!transactionDate.substring(8, 10).equals(lastDate)) {
                        model.addRow(new String[] { "", "", "", "", "", "", "", "", "" });
                    }
                }
                lastDate = transactionDate.substring(8, 10);

                String foodName = resultSet.getString("f.nama");
                int quantity = resultSet.getInt("tf.qty");
                String totalPrice = formatCurrency(resultSet.getInt("f.harga * tf.qty"));
                String kota = resultSet.getString("c.kota");
                String nama = resultSet.getString("c.nama");
                String paymentMethod = resultSet.getString("t.payment_method");
                model.addRow(new Object[] { transactionDate, kota, nama, foodName, quantity, totalPrice, paymentMethod });
            }
            conn.close();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    public void printTableTickets(int id, JTable table, DefaultTableModel model) {
        String[] columns = { "Transaction Date", "City", "Cinema", "Movie Name", "Showtime", "Seat", "Class Type", "Total Price", "Payment method" };
        model.setColumnIdentifiers(columns);
        try {
            String sql = "SELECT t.transaction_date, m.judul, j.waktu, GROUP_CONCAT(se.kode SEPARATOR ', '), s.studio_class, SUM(j.harga), c.kota, c.nama, t.payment_method FROM transaction t " +
                "JOIN transaction_jadwal tj ON tj.id_transaction = t.id_transaction " + 
                "JOIN jadwal j ON j.id_jadwal = tj.id_jadwal " + 
                "JOIN movie m ON m.id_movie = j.id_movie " +
                "JOIN user u ON u.id_user = t.id_user " +
                "JOIN studio s ON s.id_studio = j.id_studio " +
                "JOIN cinema c ON s.id_cinema = c.id_cinema " +
                "JOIN seat se ON tj.id_seat = se.id_seat " +
                "WHERE u.id_user = " + id + " " +
                "GROUP BY t.id_transaction " +
                "ORDER BY t.transaction_date;";
            PreparedStatement statement = conn.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            String lastDate = "x";
            while (resultSet.next()) {
                String transactionDate = resultSet.getString("t.transaction_date");
                if (!lastDate.equals("x")) {
                    if (!transactionDate.substring(8, 10).equals(lastDate)) {
                        model.addRow(new String[] { "", "", "", "", "", "", "", "", "" });
                    }
                }
                lastDate = transactionDate.substring(8, 10);

                String movieName = resultSet.getString("m.judul");
                String showtime = resultSet.getString("j.waktu");
                String seat = resultSet.getString("GROUP_CONCAT(se.kode SEPARATOR ', ')");
                String classType = resultSet.getString("s.studio_class");
                String totalPrice = formatCurrency(resultSet.getInt("SUM(j.harga)"));
                String kota = resultSet.getString("c.kota");
                String nama = resultSet.getString("c.nama");
                String paymentMethod = resultSet.getString("t.payment_method");
                model.addRow(new Object[] { transactionDate, kota, nama, movieName, showtime, seat, classType, totalPrice, paymentMethod });
            }
            conn.close();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    public int checkMembership(String username) {
        try {
            conn.open();
            String selectQuery = "SELECT `membership_status` FROM user WHERE username=? AND membership_status = 1";
            PreparedStatement preparedStatement = conn.connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return 0;
            }
            resultSet.close();
            conn.close();
            return 1;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
            return -99;
        }
    }

    public int raiseMembership(String username) {
        try {
            int status = checkMembership(username);
            if (status == 0) {
                conn.open();
                String updateQuery = "UPDATE user SET `membership_status` = 1, `membership_expiry_date` = ? WHERE username = ?";
                PreparedStatement preparedStatement = conn.connection.prepareStatement(updateQuery);
                LocalDate date = LocalDate.now();
                LocalDate newDate = date.plusDays(30);
                preparedStatement.setDate(1, Date.valueOf(newDate));
                preparedStatement.setString(2, username);

                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();
                conn.close();
                if (rowsAffected > 0) {
                    UserDataSingleton.getInstance().setMembership_status(1);
                    UserDataSingleton.getInstance().setMembership_expiry_date(Date.valueOf(newDate));
                    return 1;
                }
            }else{
                return -1;
            }
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
        }
        return -99;
    }

    public int extendMembership(String username) {
        try {
            int status = checkMembership(username);
            if (status == 1) {
                conn.open();
                String selectQuery = "SELECT `membership_expiry_date` FROM user WHERE username=? AND membership_status = 1";
                PreparedStatement preparedStatement = conn.connection.prepareStatement(selectQuery);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Date date = resultSet.getDate("membership_expiry_date");
                LocalDate localDate = dateToLocalDate(date);
                LocalDate newDate = localDate.plusDays(30);
                resultSet.close();
                preparedStatement.close();
                String updateQuery = "UPDATE user SET membership_expiry_date = ? WHERE username = ?";
                PreparedStatement preparedStatement2 = conn.connection.prepareStatement(updateQuery);
                preparedStatement2.setDate(1, Date.valueOf(newDate));
                preparedStatement2.setString(2, username);
                int rowsAffected = preparedStatement2.executeUpdate();
                preparedStatement2.close();
                conn.close();
                if (rowsAffected > 0) {
                    UserDataSingleton.getInstance().setMembership_expiry_date(Date.valueOf(newDate));
                    return 1;
                }
            } else {
                return 0;
            }
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
        }
        return -99;
    }

    public int revokeMembership(String username) {
        try {
            int status = checkMembership(username);
            if (status == 1) {
                conn.open();
                String updateQuery = "UPDATE user SET membership_status = 0, membership_expiry_date = ? WHERE username = ?";
                try (PreparedStatement preparedStatement = conn.connection.prepareStatement(updateQuery)) {
                    preparedStatement.setDate(1, null);
                    preparedStatement.setString(2, username);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        UserDataSingleton.getInstance().setMembership_status(0);
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }else{
                return -2;
            }
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
        }
        return -99;
    }
    public int checkExpiredMembership(String username) {
        try {
            conn.open();
            String selectQuery = "SELECT `membership_expiry_date` FROM user WHERE username=? AND membership_status = 1";
            PreparedStatement preparedStatement = conn.connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return 0;
            }
            resultSet.next();
            Date date = resultSet.getDate("membership_expiry_date");
            LocalDate localDate = dateToLocalDate(date);
            LocalDate now = LocalDate.now();
            int selisihHarinya = (int) ChronoUnit.DAYS.between(now, localDate);
            if (localDate.isBefore(now)) {
                return 1;
            }
            resultSet.close();
            conn.close();
            return selisihHarinya;
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            ex.printStackTrace();
            return -99;
        }
    }
    public String[] listUser(){
        try {
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `username` FROM `user` WHERE `user_type` = 1");

            ArrayList<String> listUser = new ArrayList<String>();
            while (result.next()) {
                listUser.add(result.getString("username"));
            }
            result.close();
            statement.close();
            conn.close();

            return listUser.toArray(new String[listUser.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }
}