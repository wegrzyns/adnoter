package legacyvideo;

import org.opencv.core.Mat;

public class AdnFrame {
    private Mat frame;
    private double frameNumber;

    public AdnFrame() {
    }

    public AdnFrame(Mat frame, double frameNumber) {
        this.frame = frame;
        this.frameNumber = frameNumber;
    }

    public Mat getFrame() {
        return frame;
    }

    public void setFrame(Mat frame) {
        this.frame = frame;
    }

    public double getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(double frameNumber) {
        this.frameNumber = frameNumber;
    }
}
