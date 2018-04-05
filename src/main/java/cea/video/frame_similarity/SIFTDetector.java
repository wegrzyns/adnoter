package cea.video.frame_similarity;

import cea.video.frame_similarity.feature.Feature;
import cea.video.frame_similarity.feature.SIFTFeature;
import cea.video.model.Chunk;
import cea.video.model.FeatureType;
import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import cea.video.slide_region.DefaultManager;
import cea.video.slide_region.SlideRegionManager;
import javafx.util.Pair;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SIFTDetector extends FrameSimilarityDetector {

    @Override
    protected Feature feature() {
        return new SIFTFeature();
    }
}
