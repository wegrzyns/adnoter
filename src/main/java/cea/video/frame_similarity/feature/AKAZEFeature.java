package cea.video.frame_similarity.feature;

import org.opencv.features2d.AKAZE;
import org.opencv.features2d.Feature2D;

public class AKAZEFeature extends OpenCVFeature {

    private static final AKAZE akaze = AKAZE.create();

    @Override
    public FeatureType featureType() {
        return FeatureType.AKAZE;
    }

    @Override
    public Feature2D feature() {
        return akaze;
    }
}
