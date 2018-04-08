package cea.video.frame_similarity.feature;

import cea.video.model.Frame;
import cea.video.model.SlideRegion;

public abstract class Feature implements AutoCloseable {

    public synchronized Feature computeFeature(Frame frame, SlideRegion slideRegionMask) {
        Feature feature = detectFeature(frame, slideRegionMask);
        feature.fillFrameWithComputedFeature(frame);

        return feature;
    }

    private void fillFrameWithComputedFeature(Frame frame) {
        frame.setFeatureComputed(featureType());
        frame.addFeature(featureType(), this);

    }

    public abstract Feature detectFeature(Frame frame, SlideRegion slideRegionMask);
    public abstract int matchFeatures(Feature feature1, Feature feature2);

    public abstract FeatureType featureType();


}
