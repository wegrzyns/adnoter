package video.detector;

import video.model.CEAFrame;
import video.model.CEASlideRegion;

public interface SlideRegionManager {

    CEASlideRegion getSlideRegion(CEAFrame frame);
}
