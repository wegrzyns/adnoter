package video.model;

public class ADNDetection {

    private ADNFrame frame;
    private ADNDetectionType type;

    public ADNDetection(ADNFrame frame, ADNDetectionType type) {
        this.frame = frame;
        this.type = type;
    }

    public ADNFrame getFrame() {
        return frame;
    }

    public void setFrame(ADNFrame frame) {
        this.frame = frame;
    }

    public ADNDetectionType getType() {
        return type;
    }

    public void setType(ADNDetectionType type) {
        this.type = type;
    }
}
