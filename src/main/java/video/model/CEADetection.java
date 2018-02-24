package video.model;

public class CEADetection {

    private CEAFrame frame;
    private CEADetectionType type;

    public CEADetection(CEAFrame frame, CEADetectionType type) {
        this.frame = frame;
        this.type = type;
    }

    public CEAFrame getFrame() {
        return frame;
    }

    public void setFrame(CEAFrame frame) {
        this.frame = frame;
    }

    public CEADetectionType getType() {
        return type;
    }

    public void setType(CEADetectionType type) {
        this.type = type;
    }
}
