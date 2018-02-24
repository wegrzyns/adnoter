package legacyvideo;

import org.opencv.core.Mat;

public class AdnoterFrame {

    private Mat originalFrame;
    private Mat modifiedFrame;
    private Mat modifiedFrame2;

    public AdnoterFrame() {
    }

    public AdnoterFrame(Mat originalFrame, Mat modifiedFrame) {
        this.originalFrame = originalFrame;
        this.modifiedFrame = modifiedFrame;
    }

    public Mat getOriginalFrame() {
        return originalFrame;
    }

    public void setOriginalFrame(Mat originalFrame) {
        this.originalFrame = originalFrame;
    }

    public Mat getModifiedFrame() {
        return modifiedFrame;
    }

    public void setModifiedFrame(Mat modifiedFrame) {
        this.modifiedFrame = modifiedFrame;
    }

    public Mat getModifiedFrame2() {
        return modifiedFrame2;
    }

    public void setModifiedFrame2(Mat modifiedFrame2) {
        this.modifiedFrame2 = modifiedFrame2;
    }
}
