package video.model;

import org.opencv.core.Mat;

import java.time.LocalTime;

public class ADNFrame {

    private Mat frame;
    private int offset;
    private LocalTime timestamp;

    public ADNFrame(Mat frame, int offset, LocalTime timestamp) {
        this.frame = frame;
        this.offset = offset;
        this.timestamp = timestamp;
    }

    public Mat getFrame() {
        return frame;
    }

    public void setFrame(Mat frame) {
        this.frame = frame;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }
}
