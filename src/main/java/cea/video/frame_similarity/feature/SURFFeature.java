package cea.video.frame_similarity.feature;

import org.opencv.features2d.AKAZE;
import org.opencv.features2d.Feature2D;
import org.opencv.xfeatures2d.SURF;

public class SURFFeature extends OpenCVFeature {

    private static final SURF surf = SURF.create();

    @Override
    public FeatureType featureType() {
        return FeatureType.SURF;
    }

    @Override
    public Feature2D feature() {
        return surf;
    }
}
