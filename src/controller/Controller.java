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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import src.model.Cinema;
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

public class Controller {
    static DatabaseHandler conn = new DatabaseHandler();

    public Controller() { }

    public void fetchData() {
        Data.movies = getMovies();
    }

    // Seat area
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
            case REGULER: seats = new Seat[9][15]; break;
            case LUXE: seats = new Seat[8][8]; break;
            case JUNIOR: seats = new Seat[9][10]; break;
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

    public Seat[][] getSeatFromJadwal(String idJadwal) {
        Seat[][] seats = new Seat[1][1];

        String idStudio = idJadwal.substring(3, 13);

        Studio studio = getStudioById(idStudio);
        switch (studio.getStudioClass()) {
            case REGULER: seats = new Seat[9][15]; break;
            case LUXE: seats = new Seat[8][8]; break;
            case JUNIOR: seats = new Seat[9][10]; break;
            case VIP: seats = new Seat[5][5]; break;
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
                        SeatStatusInterface.AVAILABLE
                    );
                }
            }

            conn.close();

            statement = conn.connection.createStatement();
            result = statement.executeQuery(
                "SELECT `id_seat` FROM `transaction_jadwal` WHERE `id_jadwal`='" + idJadwal + 
                    "' ORDER BY `id_seat` ASC;"
            );

            if (!result.isBeforeFirst()) {
                return seats;
            }

            result.next();

            mainloop:for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    if (seats[i][j].getIdSeat().equals(result.getString("id_seat"))) {
                        seats[i][j].setSeatStatus(SeatStatusInterface.TAKEN);
                        if (result.isLast()) {
                            break mainloop;
                        }
                        else {
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

        main:for (int i = 0; i < seatIds.size(); i++) {
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
                "SELECT * FROM `jadwal` WHERE `id_jadwal`='" + idJadwal + "';"
            );

            result.next();

            Jadwal jadwal = new Jadwal(
                result.getString("id_jadwal"),
                result.getString("id_movie"),
                result.getString("id_studio"),
                result.getInt("harga"),
                result.getTimestamp("waktu").toLocalDateTime(),
                getSeatFromJadwal(result.getString("id_jadwal"))
            );

            

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

    public int addNewJadwal(String idJadwal, String idStudio, String idMovie, String harga, String tanggal, String jam) {
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
            tanggalWaktu
        );
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
                        getSeatFromJadwal(result.getString("id_jadwal"))
                    )
                );
            }

            if (listJadwal.size() == 0) { return null; }
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
            // output[i][0] = listJadwal[i].getWaktu().format(DateTimeFormatter.ofPattern("dd MMMM hh:mm"));
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
            item += " | " +String.valueOf(listJadwal[i].getHarga());

            output[i][0] = item;
        }

        return output;
    }

    public Movie[] extractMoviesFromListJadwal(Jadwal[] arrJadwal) {
        if (arrJadwal == null) { return null; }

        ArrayList<Movie> movies = new ArrayList<Movie>();
        
        ArrayList<String> idsMovie = new ArrayList<String>();
        for (Jadwal jadwal : arrJadwal) {
            if (!idsMovie.contains(jadwal.getIdMovie())) {
                idsMovie.add(jadwal.getIdMovie());
            }
        }

        for (String idMovie : idsMovie) {
            for (int i = 0; i < Data.movies.length; i++) {
                if (Data.movies[i].getIdMovie().equals(idMovie)) {
                    movies.add(Data.movies[i]);
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
            getStudioType(studioType)
        );
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
                "UPDATE `studio` SET `is_deleted`=1 WHERE `id_studio`='" + idStudio + "';"
            );

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
                fotoMovie
            );

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
                    fotoMovie
                );

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
        if (Data.movies == null) { return null; }

        ArrayList<Movie> movieList = new ArrayList<Movie>();

        if (input.equals("")) {
            for (int i = 0; i < limit && i < Data.movies.length; i++) {
                movieList.add(Data.movies[i]);
            }

            return movieList.toArray(new Movie[movieList.size()]);
        }

        for (Movie movie : Data.movies) {
            if (movie.getJudul().toLowerCase().contains(input.toLowerCase())) {
                movieList.add(movie);
            }
        }

        if (movieList.isEmpty()) { return null; }

        return movieList.toArray(new Movie[movieList.size()]);
    }

    public boolean isMovieExists(String idMovie) {
        return isMovieExists(idMovie, false);
    }

    private boolean isMovieExists(String idMovie, boolean includeDeleted) {
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
                        result.getString("id_user"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("profile_name"),
                        result.getString("email"),
                        result.getString("phone_no"),
                        result.getString("address"),
                        null
                    );
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
                        result.getInt("poin")
                    );
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

    public int pesanTiket(String idCustomer, Jadwal jadwal, Seat[] bookedSeat) {
        System.out.println(idCustomer);
        if (idCustomer == null || idCustomer.equals("")) {
            return -1;
        }

        if (jadwal == null) {
            return -2;
        }

        if (bookedSeat == null) {
            return -3;
        }

        try {
            String idTransaction = createTransactionId();
            conn.open();
            
            Statement statement = conn.connection.createStatement();
            statement.executeUpdate(
                "INSERT INTO `transaction` (`id_transaction`, `id_user`, `transaction_date`) " +
                    "VALUES ('" + idTransaction + "', '" + idCustomer + "', now());"
            );

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
                "SELECT `id_transaction` FROM `transaction` ORDER BY `id_transaction` DESC LIMIT 1;"
            );

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

    // Line Ini Jangan dihapus yak sementara yak biar ga pusing
    public int totalPoinMembership(User user) {
        if (user instanceof MembershipCustomer) {
            MembershipCustomer membershipCustomer = (MembershipCustomer) user;
            return membershipCustomer.getPoin();
        } else {
            return 0;
        }
    }

    public MembershipCustomer registerMembership(String idUser, String username, String password, String profileName, String email,
            String phoneNumber, String address, int poin) {
        MembershipCustomer membershipCustomer = new MembershipCustomer(idUser, username, password, profileName, email,
                phoneNumber, address, null, poin);
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

        fetchData();
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
    
    //Function tampilkan list
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
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `user` WHERE `username`='" + UserDataSingleton.getInstance().getUsername() + "' AND `password`='" + sha256(oldPassword)
                            + "'");
            if (!result.isBeforeFirst()) {
                return 0;
            }
            result.next();
            String sql = "UPDATE `user` SET `username`=?, `password`=?, `profile_name`=?, `email`=?, `phoneNo`=?, `address`=? WHERE `username`=?;";
            PreparedStatement ps = conn.connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, sha256(newPassword));
            ps.setString(3, profileName);
            ps.setString(4, email);
            ps.setString(5, phoneNo);
            ps.setString(6, address);
            ps.setString(7, UserDataSingleton.getInstance().getUsername());
            
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
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

    public String[] listJam(String movie){
        try{
            conn.open();
            Statement statement = conn.connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT `waktu` FROM `jadwal` WHERE `id_movie`='" + movie + "'");

            ArrayList<String> listJam = new ArrayList<String>();
            while (result.next()) {
                listJam.add(result.getString("jam"));
            }
            result.close();
            statement.close();
            conn.close();

            return listJam.toArray(new String[listJam.size()]);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
            return null;
        }
    }
}
