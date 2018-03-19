package cea.video.slide_region;

import cea.video.model.CEAFrame;
import cea.video.model.CEASlideRegion;

public class DefaultManager implements SlideRegionManager {

    @Override
    public CEASlideRegion getSlideRegion(CEAFrame frame) {
        SlideRegionDetector slideRegionDetector = new DefaultDetector();
        return slideRegionDetector.detect(frame);
    }
}
