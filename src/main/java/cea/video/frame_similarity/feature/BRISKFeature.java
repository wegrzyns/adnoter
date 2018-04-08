package cea.video.frame_similarity.feature;

import org.opencv.features2d.BRISK;
import org.opencv.features2d.Feature2D;

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
