package src.model.studio;

public class Studio implements StudioTypeInterface {
    private String idStudio;
    private StudioClassEnum studioClass;
    private int studioType;

    public Studio(String idStudio, StudioClassEnum studioClass, int studioType) {
        this.idStudio = idStudio;
        this.studioClass = studioClass;
        this.studioType = studioType;
    }

    public String getIdStudio() {
        return this.idStudio;
    }

    public StudioClassEnum getStudioClass() {
        return this.studioClass;
    }

    public int getStudioType() {
        return this.studioType;
    }
}
