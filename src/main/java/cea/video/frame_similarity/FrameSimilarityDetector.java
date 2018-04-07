package cea.video.frame_similarity;

import cea.video.frame_similarity.feature.*;
import cea.video.model.Chunk;
import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import cea.video.slide_region.DefaultManager;
import cea.video.slide_region.SlideRegionManager;
import javafx.util.Pair;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FrameSimilarityDetector {

    private FeatureType featureType;

    public FrameSimilarityDetector(FeatureType featureType) {
        this.featureType = featureType;
    }

    private Feature createFeature() throws Exception {
        switch (featureType) {
            case SIFT: return new SIFTFeature();
            case PixelDiff: return new PixelDiffFeature();
            case BRISK: return new BRISKFeature();
            case AKAZE: return new AKAZEFeature();
            case SURF: return new SURFFeature();
        }
        //TODO: default, configurable?
        throw new Exception(String.format("Missing feature type creation case, feature: %s", featureType.name()));
    }

    public Chunk computeChunkFramesSimiliarity(Chunk chunk) {
        SlideRegionManager slideRegionManager = new DefaultManager();
        List<Frame> chunkFrames = chunk.framesAsList();

        List<Feature> features = chunkFrames.stream()
                .map(frame -> {
                    if(frame.isFeatureComputed(featureType)) return frame.getFeature(featureType);
                    SlideRegion slideRegion = slideRegionManager.getSlideRegion(frame);
                    if(slideRegion == null) return null;
                    try {
                        Feature feature = createFeature();
                        return feature.computeFeature(frame, slideRegion);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        if(missingSlideRegion(features)) return chunk;

        IntStream.rangeClosed(0, 2)
                .filter(i -> frameMatchNotComputed(chunk.getFrameMatch(i)))
                .forEach(i -> chunk.setFrameMatch(i, matchFramesDescriptors(i, features)));

        return chunk;
    }

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

    public String featureDetectorName() {
        return featureType.name();
    }
}
