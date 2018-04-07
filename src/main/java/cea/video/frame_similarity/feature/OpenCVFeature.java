package cea.video.frame_similarity.feature;

import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.imgproc.Imgproc;

public abstract class OpenCVFeature extends Feature {

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
        feature().detect(frame, toRet, frameRegionMask);
        return toRet;
    }

    private Mat keyPointDescriptors(Mat frame, MatOfKeyPoint siftFeatures) {
        Mat keyPointsDescriptors = new Mat();
        feature().compute(frame, siftFeatures, keyPointsDescriptors);
        return keyPointsDescriptors;
    }


    public int matchFeatures(Feature feature1, Feature feature2) {
        OpenCVFeature openCVFeature1 = (OpenCVFeature) feature1;
        OpenCVFeature openCVFeature2 = (OpenCVFeature) feature2;
        return matchFramesDescriptors(openCVFeature1.getKeyPointDescriptors(), openCVFeature2.getKeyPointDescriptors());
    }


    protected int matchFramesDescriptors(Mat frame1KeyPointDescriptors, Mat frame2KeyPointDescriptors) {
        if(keyPointDescriptorEmpty(frame1KeyPointDescriptors) || keyPointDescriptorEmpty(frame2KeyPointDescriptors)) {
            return 0;
        }
        MatOfDMatch descriptorMatches = new MatOfDMatch();
        BFMatcher bfMatcher = BFMatcher.create(BFMatcher.BRUTEFORCE, true);
        bfMatcher.match(frame1KeyPointDescriptors, frame2KeyPointDescriptors, descriptorMatches);

        return descriptorMatches.toArray().length;
    }

    private boolean keyPointDescriptorEmpty(Mat keyPointsDescriptors) {
        return keyPointsDescriptors.rows() == 0 || keyPointsDescriptors.cols() == 0;
    }

    @Override
    public abstract FeatureType featureType();

    public abstract Feature2D feature();

    @Override
    public void close() throws Exception {
        keyPointDescriptors.release();
    }

    private Mat getKeyPointDescriptors() {
        return keyPointDescriptors;
    }

    public void setKeyPointDescriptors(Mat keyPointDescriptors) {
        this.keyPointDescriptors = keyPointDescriptors;
    }
}
