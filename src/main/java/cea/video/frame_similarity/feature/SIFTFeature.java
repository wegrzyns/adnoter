package cea.video.frame_similarity.feature;

import cea.video.model.FeatureType;
import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;

public class SIFTFeature extends Feature {

    private static final SIFT sift = SIFT.create();
    private Mat keyPointDescriptors;

    @Override
    public Feature detectFeature(Frame frame, SlideRegion slideRegion) {
        Mat preparedFrame = prepareFrame(frame);
        MatOfKeyPoint siftFeatures = detectFeatures(preparedFrame, slideRegion.getSlideRegionMask());
        this.keyPointDescriptors = keyPointDescriptors(preparedFrame, siftFeatures);
        preparedFrame.release();
        try {
            slideRegion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private Mat prepareFrame(Frame ceaframe) {
        Mat frame = ceaframe.getFrame().clone();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame,  new Size(21, 21), 0);
        return frame;
    }

    private MatOfKeyPoint detectFeatures(Mat frame, Mat frameRegionMask) {
        MatOfKeyPoint toRet = new MatOfKeyPoint();
        sift.detect(frame, toRet, frameRegionMask);
        return toRet;
    }

    private Mat keyPointDescriptors(Mat frame, MatOfKeyPoint siftFeatures) {
        Mat keyPointsDescriptors = new Mat();
        sift.compute(frame, siftFeatures, keyPointsDescriptors);
        return keyPointsDescriptors;
    }

    @Override
    public int matchFeatures(Feature feature1, Feature feature2) {
        SIFTFeature siftFeatureFeature1 = (SIFTFeature) feature1;
        SIFTFeature siftFeatureFeature2 = (SIFTFeature) feature2;
        return matchFramesDescriptors(siftFeatureFeature1.getKeyPointDescriptors(), siftFeatureFeature2.getKeyPointDescriptors());
    }

    protected int matchFramesDescriptors(Mat frame1KeyPointDescriptors, Mat frame2KeyPointDescriptors) {
        MatOfDMatch descriptorMatches = new MatOfDMatch();
        BFMatcher bfMatcher = BFMatcher.create(BFMatcher.BRUTEFORCE, true);
        bfMatcher.match(frame1KeyPointDescriptors, frame2KeyPointDescriptors, descriptorMatches);

        return descriptorMatches.toArray().length;
    }

    public Mat getKeyPointDescriptors() {
        return keyPointDescriptors;
    }

    public void setKeyPointDescriptors(Mat keyPointDescriptors) {
        this.keyPointDescriptors = keyPointDescriptors;
    }

    @Override
    public FeatureType featureType() {
        return FeatureType.SIFT;
    }

    @Override
    public void close() throws Exception {
        keyPointDescriptors.release();
    }
}
