package src.model;

public class FnB {

    private String nama;
    private int harga;
    private String description;
    
    public FnB(String nama, int harga, String description) {
        this.nama = nama;
        this.harga = harga;
        this.description = description;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}