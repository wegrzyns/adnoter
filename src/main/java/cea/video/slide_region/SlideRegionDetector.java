package cea.video.slide_region;

import cea.video.model.Frame;
import cea.video.model.SlideRegion;

public interface SlideRegionDetector {

    SlideRegion detect(Frame frame);
}
