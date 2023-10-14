package model;

import java.util.ArrayList;

import model.studio.Studio;

public class Cinema {
    private String nama;
    private String alamat;
    private String kota;
    ArrayList<Studio> studio;
    
    public Cinema(String nama, String alamat, String kota, ArrayList<Studio> studio) {
        this.nama = nama;
        this.alamat = alamat;
        this.kota = kota;
        this.studio = studio;
    }

    public String getNama() {
        return this.nama;
    }

    public String getAlamat() {
        return this.alamat;
    }

    public String getKota() {
        return this.kota;
    }

    public ArrayList<Studio> getStudio() {
        return this.studio;
    }
}
