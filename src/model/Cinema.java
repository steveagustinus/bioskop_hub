package src.model;

import java.io.File;
import java.util.ArrayList;

import src.model.studio.Studio;

public class Cinema {
    private String id;
    private String nama;
    private String alamat;
    private String kota;
    private File image;
    private ArrayList<Studio> studio;
    
    public Cinema(String id, String nama, String alamat, String kota, File image, ArrayList<Studio> studio) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.kota = kota;
        this.image = image;
        this.studio = studio;
    }

    public String getId() {
        return this.id;
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

    public File getImage() {
        return this.image;
    }

    public ArrayList<Studio> getStudio() {
        return this.studio;
    }
}
