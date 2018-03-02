package video.detector;

import video.model.CEAFrame;
import video.model.CEASlideRegion;

public interface SlideRegionDetector {

    CEASlideRegion detect(CEAFrame frame);
}
