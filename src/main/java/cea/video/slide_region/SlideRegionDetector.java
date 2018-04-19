package cea.video.slide_region;

import cea.Util.*;
import cea.video.model.Frame;
import cea.video.model.SlideRegion;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class SlideRegionDetector {

    private static Logger logger = LoggerFactory.getLogger(SlideRegionDetector.class);


    protected static final int BINARIZATION_INTENSITY_ABOVE_THRESHOLD = 255;
    protected static final int MINIMAL_FRAME_AREA_FRACTION = ConfigurationUtil.configuration().getInt("slideRegion.minimalFrameAreaFraction");
    protected static final double STANDARD_ASPECT_RATIO = 4.0/3.0;
    protected static final double FULL_HD_ASPECT_RATIO = 16.0/9.0;
    protected static final double ASPECT_RATIO_TOLERANCE = ConfigurationUtil.configuration().getDouble("slideRegion.aspectRatioTolerance");
    protected static final double MIN_STANDARD_ASPECT_RATIO = STANDARD_ASPECT_RATIO * (1 - ASPECT_RATIO_TOLERANCE);
    protected static final double MAX_STANDARD_ASPECT_RATIO = STANDARD_ASPECT_RATIO * (1 + ASPECT_RATIO_TOLERANCE);
    protected static final double MIN_FULL_HD_ASPECT_RATIO = FULL_HD_ASPECT_RATIO * (1 - ASPECT_RATIO_TOLERANCE);
    protected static final double MAX_FULL_HD_ASPECT_RATIO = FULL_HD_ASPECT_RATIO * (1 + ASPECT_RATIO_TOLERANCE);
    protected static final int GAUSSIAN_BLUR_KERNEL_SIZE = ConfigurationUtil.configuration().getInt("slideRegion.gaussianBlurKernelSize");
    protected static final int MORPHOLOGY_CLOSE_KERNEL_SIZE = ConfigurationUtil.configuration().getInt("slideRegion.morphologyCloseKernelSize");

    public SlideRegion detect(Frame frame) {
        Mat copiedFrame = frame.getFrame().clone();

        double frameArea;
        List<MatOfPoint> contours;
        MatOfPoint slideRegionContour;
        Mat slideRegionMask;

        contours = findContours(copiedFrame);

        //--- visualization TODO: handle this better!
//        List<MatOfPoint> arg = new ArrayList<>();
//        arg.add(contours);
//        ImageDisplayUtil.drawContoursAndKeypoints(copiedFrame, false, contours, null);
//        ImageDisplayUtil.showResult(copiedFrame, frame.getTimestamp());

        //--- end visualization

        frameArea = frame.getVideo().getFrameArea();
        slideRegionContour = selectSlideRegionContour(contours, frameArea);

        if(slideRegionContour != null) {

//            VISUALIZATION
//            List<MatOfPoint> arg = new ArrayList<>();
//            arg.add(slideRegionContour);
//            ImageDisplayUtil.drawContoursAndKeypoints(copiedFrame, false, arg, null);
//            ImageDisplayUtil.showResult(copiedFrame, frame.getTimestamp());
//            VISUALIZATION
            copiedFrame.release();

            slideRegionMask = prepareMask(slideRegionContour, frame);
            return new SlideRegion(slideRegionMask, slideRegionContour);
        }
        copiedFrame.release();

        logger.debug(String.format("No slide region found for frame at %s", frame.getTimestamp()));
        return null;
    }

    protected abstract List<MatOfPoint> findContours(Mat frame);

    protected MatOfPoint selectSlideRegionContour(List<MatOfPoint> contours, double frameArea) {
        MatOfPoint toRet = contours.stream()
                .map(this::approximateContour)
//                .filter(this::isAtLeastQuadrangle)
                .filter(this::isQuadrangle)
                .filter(contour -> isAreaSufficient(contour, frameArea))
                .filter(this::isBoundingBoxAreaCorrect)
//                .filter(this::isRectangle)
                .filter(this::isAspectRatioCorrect)
                .max(Comparator.comparing(GenericUtil.cache(contour -> confidenceScore(contour, frameArea))))
                .orElse(null);

        contours.remove(toRet);
        contours.forEach(Mat::release);

        return toRet;
    }

    //TODO: better log represenation of slide region operations, non trivial
    public static List<String> slideRegionOperations() {
        List<String> toRet = new ArrayList<>();
        toRet.add("contour approximation");
//        toRet.add("at least quadrangle check");
        toRet.add("quadrangle check");
        toRet.add("bounding area size check");
//        toRet.add("rectangle check");
        toRet.add("aspect ratio check");
        toRet.set(toRet.size()-1, toRet.get(toRet.size()-1) + "\n");
        return toRet;
    }

    protected MatOfPoint approximateContour(MatOfPoint contour) {
        MatOfPoint2f contour2f = TypeUtil.convertMatToPoint2f(contour);
        double contourLength = Imgproc.arcLength(contour2f, true);
        Imgproc.approxPolyDP(contour2f, contour2f, contourLength*0.01, true);
        return TypeUtil.convertPoint2fToMat(contour2f);
    }

    protected boolean isAtLeastQuadrangle(MatOfPoint matOfPoint) {
        return matOfPoint.toList().size() >= 4 && matOfPoint.toList().size() <= 5;
    }

    protected boolean isQuadrangle(MatOfPoint matOfPoint) {
        return matOfPoint.toList().size() == 4;
    }

    protected boolean isAreaSufficient(MatOfPoint matOfPoint, double frameArea) {
        return contourArea(matOfPoint) > frameArea/MINIMAL_FRAME_AREA_FRACTION;
    }

    //checks for diagonal equality, this does not guarantee rectangle but close enough?
    protected boolean isRectangle(MatOfPoint rectangleVertices) {
        Point[] points = rectangleVertices.toArray();
        double diagonal1Len = Point2D.distance(points[0].x, points[0].y, points[2].x, points[2].y);
        double diagonal2Len = Point2D.distance(points[1].x, points[1].y, points[3].x, points[3].y);
        return NumberUtil.between(diagonal1Len, diagonal2Len * 0.97, diagonal2Len * 1.03);
    }

    //maybe it is better to not use boundingRect and do this on our rect?
    protected boolean isAspectRatioCorrect(MatOfPoint matOfPoint) {
        Rect rectangle = Imgproc.boundingRect(matOfPoint);
        double aspectRatio = rectangle.width / (double)rectangle.height;
        return NumberUtil.between(aspectRatio, MIN_STANDARD_ASPECT_RATIO, MAX_STANDARD_ASPECT_RATIO)
                || NumberUtil.between(aspectRatio, MIN_FULL_HD_ASPECT_RATIO, MAX_FULL_HD_ASPECT_RATIO);
    }

    protected boolean isBoundingBoxAreaCorrect(MatOfPoint matOfPoint) {
        return contourArea(matOfPoint) * 2 >= boundingBoxArea(matOfPoint);
    }

    protected Mat prepareMask(MatOfPoint slideRegion, Frame originalFrame) {
        Mat slideRegionMask = Mat.zeros(originalFrame.getFrame().rows(), originalFrame.getFrame().cols(), CvType.CV_8UC(1));
        Rect boundingRect = Imgproc.boundingRect(slideRegion);
        MatOfPoint boundingRectMatOfPoint = TypeUtil.convertRectToMatOfPoint(boundingRect);
        Imgproc.fillConvexPoly(slideRegionMask, boundingRectMatOfPoint, new Scalar(1));
        return slideRegionMask;
    }

    private double contourArea(MatOfPoint matOfPoint) {
        Mat contour = TypeUtil.convertPointMatToMat(matOfPoint);
        return Imgproc.contourArea(contour);
    }

    private double boundingBoxArea(MatOfPoint matOfPoint) {
        Rect boundingRect = Imgproc.boundingRect(matOfPoint);
        return boundingRect.area();
    }

    //based on Zhao, B., Lin, S., Qi, X. (2018), A novel approach to automatic detection of presentation slides in educational videos
    private double confidenceScore(MatOfPoint matOfPoint, double frameArea) {
        double param = 0.6;
        double boundingBoxArea = boundingBoxArea(matOfPoint);
        return param * contourArea(matOfPoint) / boundingBoxArea + (1 - param) * boundingBoxArea / frameArea;

    }
}
