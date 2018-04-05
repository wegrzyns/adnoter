package cea.video.frame_similarity;

import cea.video.frame_similarity.feature.Feature;
import cea.video.frame_similarity.feature.PixelDiffFeature;

public class PixelDiffDetector extends FrameSimilarityDetector {

    @Override
    protected Feature feature() {
        return new PixelDiffFeature();
    }
}
