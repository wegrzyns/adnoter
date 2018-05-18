package cea.video.frame_similarity.feature;

import cea.Util.ConfigurationUtil;
import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public abstract class Feature implements AutoCloseable {
    public Frame framee;
    public MatOfKeyPoint siftFeat;
    public Mat keyPointDescriptors;

    protected static final int GAUSSIAN_BLUR_KERNEL_SIZE = ConfigurationUtil.configuration().getInt("feature.gaussianBlurKernelSize");

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
