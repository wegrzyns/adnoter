package cea.legacyvideo;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class AdnoterFeatures {

    private Mat descriptors;
    private MatOfKeyPoint keyPoints;

    public AdnoterFeatures() {
    }

    public AdnoterFeatures(Mat descriptors, MatOfKeyPoint keyPoints) {
        this.descriptors = descriptors;
        this.keyPoints = keyPoints;
    }

    public Mat getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(Mat descriptors) {
        this.descriptors = descriptors;
    }

    public MatOfKeyPoint getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(MatOfKeyPoint keyPoints) {
        this.keyPoints = keyPoints;
    }
}
