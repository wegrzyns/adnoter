package cea.video.frame_similarity;

import cea.video.frame_similarity.feature.Feature;
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

public abstract class FrameSimilarityDetector {

    public Chunk computeChunkFramesSimiliarity(Chunk chunk) {
        SlideRegionManager slideRegionManager = new DefaultManager();
        List<Frame> chunkFrames = chunk.framesAsList();

        List<Feature> features = chunkFrames.stream()
                .map(frame -> {
                    if(frame.isFeatureComputed(featureType())) return frame.getFeature(FeatureType.SIFT);
                    SlideRegion slideRegion = slideRegionManager.getSlideRegion(frame);
                    if(slideRegion == null) return null;
                    Mat slideRegionMask = slideRegion.getSlideRegionMask();
                    return feature().detectFeature(frame, slideRegionMask);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        if(missingSlideRegion(features)) return chunk;

        IntStream.rangeClosed(0, 2)
                .filter(i -> frameMatchNotComputed(chunk.getFrameMatch(i)))
                .forEach(i -> chunk.setFrameMatch(i, matchFramesDescriptors(i, features)));

        return chunk;
    }

    protected abstract FeatureType featureType();

    protected abstract Feature feature();

    private boolean missingSlideRegion(List<Feature> framesKeyPointsDescriptors) {
        return framesKeyPointsDescriptors.size() < 3;
    }

    private int matchFramesDescriptors(int matchIndex, List<Feature> descriptors) {
        Pair<Integer, Integer> indexes = Chunk.getFrameMatchIndex(matchIndex);

        Feature feature1 = descriptors.get(indexes.getKey());
        Feature feature2 = descriptors.get(indexes.getValue());

        return feature1.matchFeatures(feature1, feature2);
    }

    private boolean frameMatchNotComputed(int frameMatchValue) {
        return frameMatchValue == Chunk.FRAME_MATCH_NOT_COMPUTED;
    }
}
