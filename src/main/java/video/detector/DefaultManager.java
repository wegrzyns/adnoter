package video.detector;

import video.model.CEAFrame;
import video.model.CEASlideRegion;

public class DefaultManager implements SlideRegionManager {

    @Override
    public CEASlideRegion getSlideRegion(CEAFrame frame) {
        return null;
    }
}
