package video.model;

import org.opencv.core.Mat;

import java.time.Duration;

public class CEAFrame {

    private Mat frame;
    private long position;
    private Duration timestamp;
    private CEAVideo video;

    public CEAFrame(Mat frame, long position, Duration timestamp, CEAVideo video) {
        this.frame = frame;
        this.position = position;
        this.timestamp = timestamp;
        this.video = video;
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

    public CEAVideo getVideo() {
        return video;
    }

    public void setVideo(CEAVideo video) {
        this.video = video;
    }
}
