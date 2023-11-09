package src.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import src.model.seat.Seat;

public class Jadwal {
    private String idJadwal;
    private String idMovie;
    private String idStudio;
    private int harga;
    private LocalDateTime waktu;
    private ArrayList<Seat> seat;

    public Jadwal(String idJadwal, String idMovie, String idStudio, int harga, LocalDateTime waktu, ArrayList<Seat> seat) {
        this.idJadwal = idJadwal;
        this.idMovie = idMovie;
        this.idStudio = idStudio;
        this.harga = harga;
        this.waktu = waktu;
        this.seat = seat;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getIdStudio() {
        return idStudio;
    }

    public void setIdStudio(String idStudio) {
        this.idStudio = idStudio;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public void setWaktu(LocalDateTime waktu) {
        this.waktu = waktu;
    }

    public ArrayList<Seat> getSeat() {
        return seat;
    }

    public void setSeat(ArrayList<Seat> seat) {
        this.seat = seat;
    }
}
