package cea.video.slide_region;

import cea.video.model.Frame;
import cea.video.model.SlideRegion;

public class DefaultManager implements SlideRegionManager {

    @Override
    public SlideRegion getSlideRegion(Frame frame) {
        SlideRegionDetector slideRegionDetector = new MultiBinarizationDetector();
        SlideRegion toRet = slideRegionDetector.detect(frame);
        if(toRet != null) {
            frame.setSlideRegionDetected();
        }
        return toRet;
    }
}
