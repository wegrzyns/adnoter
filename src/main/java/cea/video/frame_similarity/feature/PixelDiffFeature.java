package cea.video.frame_similarity.feature;

import cea.video.model.FeatureType;
import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PixelDiffFeature extends Feature {

    private Mat preparedFrame;
    private SlideRegion slideRegion;

    @Override
    public Feature detectFeature(Frame frame, SlideRegion slideRegion) {
        this.preparedFrame = prepareFrame(frame, slideRegion);
        this.slideRegion = slideRegion;
        return this;
    }

    private Mat prepareFrame(Frame ceaframe, SlideRegion slideRegion) {
        Mat frame = ceaframe.getFrame().clone();
        //TODO: check this region cutting latter(if worth time wise)
//        Mat frame = slideRegion.getMaskRegionOfIntrest(toRet);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame,  new Size(23, 23), 0);
        Imgproc.adaptiveThreshold(frame, frame, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(11, 11));
        Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_OPEN, kernel);
        kernel.release();
        return frame;
    }

    @Override
    public int matchFeatures(Feature feature1, Feature feature2) {
        PixelDiffFeature pdf1 = (PixelDiffFeature) feature1;
        PixelDiffFeature pdf2 = (PixelDiffFeature) feature2;

        int equalPixelCount = 0;
        for(int x = 0; x < pdf1.preparedFrame.cols(); x++) {
            for(int y = 0; y < pdf1.preparedFrame.rows(); y++) {
                if(pixelEquality(pdf1, pdf2, x, y)) {
                    equalPixelCount++;
                }
            }
        }

        return equalPixelCount;
    }

    private boolean pixelEquality(PixelDiffFeature pixelDiffFeature1, PixelDiffFeature pixelDiffFeature2, int x, int y){
        if( pixelDiffFeature1.slideRegion.containsPoint(x, y)
                && pixelDiffFeature2.slideRegion.containsPoint(x, y)) {
            double value1 = pixelDiffFeature1.preparedFrame.get(y, x)[0];
            double value2 = pixelDiffFeature2.preparedFrame.get(y, x)[0];

            return value1 == value2;
        }
        return false;
    }

    public Mat getPreparedFrame() {
        return preparedFrame;
    }

    public void setPreparedFrame(Mat preparedFrame) {
        this.preparedFrame = preparedFrame;
    }

    @Override
    public FeatureType featureType() {
        return FeatureType.PixelDiff;
    }

    @Override
    public void close() throws Exception {
        preparedFrame.release();
        slideRegion.close();
    }
}
