package cea.video.model;

public class Detection implements Comparable<Detection>{

    private Frame frame;
    private DetectionType type;

    public Detection(Frame frame, DetectionType type, String featureDetectorName) {
        this.frame = frame;
        this.type = type;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public DetectionType getType() {
        return type;
    }

    public void setType(DetectionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Detection{" +
                "frame=" + frame +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(Detection o) {
        return Long.compare(frame.getPosition(), o.frame.getPosition());
    }
}
