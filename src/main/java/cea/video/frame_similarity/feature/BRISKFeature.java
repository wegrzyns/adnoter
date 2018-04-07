package cea.video.frame_similarity.feature;

import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.BRISK;
import org.opencv.features2d.Feature2D;
import org.opencv.imgproc.Imgproc;

public class BRISKFeature extends OpenCVFeature {

    private static final BRISK brisk = BRISK.create();

    @Override
    public FeatureType featureType() {
        return FeatureType.BRISK;
    }

    @Override
    public Feature2D feature() {
        return brisk;
    }


}
