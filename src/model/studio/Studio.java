package src.model.studio;

public class Studio implements StudioTypeInterface {
    private String idStudio;
    private String idCinema;
    private StudioClassEnum studioClass;
    private int studioType;

    public Studio(String idStudio, String idCinema, StudioClassEnum studioClass, int studioType) {
        this.idStudio = idStudio;
        this.idCinema = idCinema;
        this.studioClass = studioClass;
        this.studioType = studioType;
    }

    public String getIdStudio() {
        return this.idStudio;
    }

    public String getIdCinema() {
        return this.idCinema;
    }
    
    public StudioClassEnum getStudioClass() {
        return this.studioClass;
    }

    public int getStudioType() {
        return this.studioType;
    }
}
