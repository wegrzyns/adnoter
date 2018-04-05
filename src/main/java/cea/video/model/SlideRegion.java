package cea.video.model;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class SlideRegion implements AutoCloseable {

    private Mat slideRegionMask;
    private MatOfPoint slideRegionContour;
    private int frameOffset;
    private int slideRegionValidityLength;

    public SlideRegion(Mat slideRegionMask, MatOfPoint slideRegionContour) {
        this.slideRegionMask = slideRegionMask;
        this.slideRegionContour = slideRegionContour;
    }

    public SlideRegion(Mat slideRegionMask, MatOfPoint slideRegionContour, int frameOffset, int slideRegionValidityLength) {
        this.slideRegionMask = slideRegionMask;
        this.frameOffset = frameOffset;
        this.slideRegionValidityLength = slideRegionValidityLength;
        this.slideRegionContour = slideRegionContour;
    }

    public Mat getSlideRegionMask() {
        return slideRegionMask;
    }

    public void setSlideRegionMask(Mat slideRegionMask) {
        this.slideRegionMask = slideRegionMask;
    }

    public int getFrameOffset() {
        return frameOffset;
    }

    public void setFrameOffset(int frameOffset) {
        this.frameOffset = frameOffset;
    }

    public int getSlideRegionValidityLength() {
        return slideRegionValidityLength;
    }

    public void setSlideRegionValidityLength(int slideRegionValidityLength) {
        this.slideRegionValidityLength = slideRegionValidityLength;
    }

    public MatOfPoint getSlideRegionContour() {
        return slideRegionContour;
    }

    public void setSlideRegionContour(MatOfPoint slideRegionContour) {
        this.slideRegionContour = slideRegionContour;
    }

    public Mat getMaskRegionOfIntrest(Mat frame) {
        Rect rect = Imgproc.boundingRect(slideRegionContour);
        return frame.submat(rect);
    }

    public boolean containsPoint(int x, int y) {
        return slideRegionMask.get(y, x)[0] == 1.0;
    }

    @Override
    public void close() throws Exception {
        slideRegionMask.release();
        slideRegionContour.release();
    }
}
