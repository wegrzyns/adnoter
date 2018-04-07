package cea.video.frame_similarity.feature;

import org.opencv.features2d.Feature2D;
import org.opencv.xfeatures2d.SIFT;

public class SIFTFeature extends OpenCVFeature {

    private static final SIFT sift = SIFT.create();

    @Override
    public FeatureType featureType() {
        return FeatureType.SIFT;
    }

    @Override
    public Feature2D feature() {
        return sift;
    }

}
