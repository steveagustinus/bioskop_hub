package src.model.studio;

public class Studio implements StudioTypeInterface {
    private StudioClassEnum studioClass;
    private int studioType;

    public Studio(StudioClassEnum studioClass, int studioType) {
        this.studioClass = studioClass;
        this.studioType = studioType;
    }

    public StudioClassEnum getStudioClass() {
        return this.studioClass;
    }

    public int getStudioType() {
        return this.studioType;
    }
}
