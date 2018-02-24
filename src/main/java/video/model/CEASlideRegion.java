package video.model;

import org.opencv.core.Mat;

public class CEASlideRegion {

    private Mat slideRegionMask;
    private int frameOffset;
    private int frameLength;

    public CEASlideRegion(Mat slideRegionMask) {
        this.slideRegionMask = slideRegionMask;
    }

    public CEASlideRegion(Mat slideRegionMask, int frameOffset, int frameLength) {
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
