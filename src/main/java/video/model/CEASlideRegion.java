package video.model;

import org.opencv.core.Mat;

public class CEASlideRegion {

    private Mat slideRegionMask;
    private int frameOffset;
    private int slideRegionValidityLength;

    public CEASlideRegion(Mat slideRegionMask) {
        this.slideRegionMask = slideRegionMask;
    }

    public CEASlideRegion(Mat slideRegionMask, int frameOffset, int slideRegionValidityLength) {
        this.slideRegionMask = slideRegionMask;
        this.frameOffset = frameOffset;
        this.slideRegionValidityLength = slideRegionValidityLength;
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
}
