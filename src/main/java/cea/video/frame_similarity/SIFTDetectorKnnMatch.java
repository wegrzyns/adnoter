package cea.video.frame_similarity;

import cea.video.frame_similarity.feature.Feature;
import cea.video.frame_similarity.feature.SIFTFeature;
import cea.video.frame_similarity.feature.SIFTFeatureKnnMatch;
import cea.video.model.FeatureType;

public class SIFTDetectorKnnMatch extends FrameSimilarityDetector {

    @Override
    protected FeatureType featureType() {
        return FeatureType.SIFT;
    }

    @Override
    protected Feature feature() {
        return new SIFTFeatureKnnMatch();
    }
}
