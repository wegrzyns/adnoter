package cea.video.frame_similarity.feature;

import cea.video.model.Frame;
import org.opencv.core.Mat;

public abstract class Feature {

    public abstract Feature detectFeature(Frame frame, Mat slideRegionMask);
    public abstract int matchFeatures(Feature feature1, Feature feature2);
}
