package video.model;

import org.opencv.core.Mat;

public class ADNSlideRegion {

    private Mat slideRegionMask;
    private int frameOffset;
    private int frameLength;

    public ADNSlideRegion(Mat slideRegionMask) {
        this.slideRegionMask = slideRegionMask;
    }

    public ADNSlideRegion(Mat slideRegionMask, int frameOffset, int frameLength) {
        this.slideRegionMask = slideRegionMask;
        this.frameOffset = frameOffset;
        this.frameLength = frameLength;
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

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }
}
