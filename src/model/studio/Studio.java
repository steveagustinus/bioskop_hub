package src.model.studio;

public class Studio implements StudioType {
    private StudioClass studioClass;
    private int studioType;

    public Studio(StudioClass studioClass, int studioType) {
        this.studioClass = studioClass;
        this.studioType = studioType;
    }

    public StudioClass getStudioClass() {
        return this.studioClass;
    }

    public int getStudioType() {
        return this.studioType;
    }
}
