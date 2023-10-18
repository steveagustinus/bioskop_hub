package src.model.movie;

import java.io.File;
import java.util.Date;

public class Movie implements MovieLanguageInterface{
    private String idMovie;
    private String judul;
    private Date releaseDate;
    private String director;
    private int language;
    private int durasi;
    private String sinopsis;
    private File fotoMovie;

    public Movie(String idMovie, String judul, Date releaseDate, String director, int language, int durasi, String sinopsis, File fotoMovie) {
        this.idMovie = idMovie;
        this.judul = judul;
        this.releaseDate = releaseDate;
        this.director = director;
        this.language = language;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.fotoMovie = fotoMovie;
    }

    public String getIdMovie() {
        return this.idMovie;
    }

    public String getJudul() {
        return judul;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public String getLanguage() {
        switch (this.language) {
            case ENGLISH: return "English";
            case JAPANESE: return "Japanese";
            case BAHASA_INDONESIA: return "Bahasa_Indonesia";
        }
        return "";
    }

    public int getDurasi() {
        return durasi;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public File getFotoMovie() {
        return this.fotoMovie;
    }
}
