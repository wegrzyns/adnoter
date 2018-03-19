package cea.video.slide_region;

import cea.video.model.CEAFrame;
import cea.video.model.CEASlideRegion;

public interface SlideRegionManager {

    CEASlideRegion getSlideRegion(CEAFrame frame);
}
