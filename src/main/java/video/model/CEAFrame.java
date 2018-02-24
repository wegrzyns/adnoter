package video.model;

import org.opencv.core.Mat;

import java.time.Duration;

public class CEAFrame {

    private Mat frame;
    private long position;
    private Duration timestamp;

    public CEAFrame(Mat frame, long position, Duration timestamp) {
        this.frame = frame;
        this.position = position;
        this.timestamp = timestamp;
    }

    public Mat getFrame() {
        return frame;
    }

    public void setFrame(Mat frame) {
        this.frame = frame;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public Duration getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Duration timestamp) {
        this.timestamp = timestamp;
    }
}
