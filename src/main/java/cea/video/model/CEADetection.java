package cea.video.model;

public class CEADetection implements Comparable<CEADetection>{

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

    @Override
    public String toString() {
        return "CEADetection{" +
                "frame=" + frame +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(CEADetection o) {
        return Long.compare(frame.getPosition(), o.frame.getPosition());
    }
}
