package video.detector;

import Util.GenericUtil;
import Util.NumberUtil;
import Util.TypeUtil;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import video.model.CEAFrame;
import video.model.CEASlideRegion;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultDetector implements SlideRegionDetector {

    private static final int INTENSITY_BINARIZATION_ABOVE_THRESHOLD = 240;
    private static final int MINIMAL_FRAME_AREA_FRACTION = 14;
    private static final double STANDARD_ASPECT_RATIO = 4.0/3.0;
    private static final double MIN_ASPECT_RATIO = STANDARD_ASPECT_RATIO * 0.70;
    private static final double MAX_ASPECT_RATIO = STANDARD_ASPECT_RATIO * 1.30;

    @Override
    public CEASlideRegion detect(CEAFrame frame) {
        Mat copiedFrame = frame.getFrame().clone();
        List<MatOfPoint> contours;
        MatOfPoint slideRegion;

        prepareFrame(copiedFrame);
        contours = findContours(copiedFrame);
        //TODO: frameArea arg nedded
        slideRegion = selectSlideRegionContour(contours, 999); //mock arg
        return null;
    }

    private void prepareFrame(Mat frame) {
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame,  new Size(7, 7), 0);
        Imgproc.threshold(frame, frame, 200, INTENSITY_BINARIZATION_ABOVE_THRESHOLD, Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(9, 9));
        Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_OPEN, kernel);
    }

    private List<MatOfPoint> findContours(Mat copiedFrame) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(copiedFrame, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    private MatOfPoint selectSlideRegionContour(List<MatOfPoint> contours, double frameArea) {
         return contours.stream()
                .map(this::approximateContour)
                .filter(this::isQuadrangle)
                .filter(contour -> isAreaSufficient(contour, frameArea))
                .filter(this::isRectangle)
                .filter(this::isAspectRatioCorrect)
                .max(Comparator.comparing(GenericUtil.cache(Imgproc::contourArea)))
                .orElse(null);
    }

    private MatOfPoint approximateContour(MatOfPoint contour) {
        MatOfPoint2f contour2f = TypeUtil.convertMatToPoint2f(contour);
        double contourLength = Imgproc.arcLength(contour2f, true);
        Imgproc.approxPolyDP(contour2f, contour2f, contourLength*0.05, true);
        return TypeUtil.convertPoint2fToMat(contour2f);
    }

    private boolean isQuadrangle(MatOfPoint matOfPoint) {
        return matOfPoint.toList().size() == 4;
    }

    private boolean isAreaSufficient(MatOfPoint matOfPoint, double frameArea) {
        Mat contour = TypeUtil.convertPointMatToMat(matOfPoint);
        double contourArea = Imgproc.contourArea(contour);
        return contourArea > frameArea/MINIMAL_FRAME_AREA_FRACTION;
    }

    //checks for diagonal equality, this does not guarantee rectangle but close enough?
    private boolean isRectangle(MatOfPoint rectangleVertices) {
        Point[] points = rectangleVertices.toArray();
        double diagonal1Len = Point2D.distance(points[0].x, points[0].y, points[2].x, points[2].y);
        double diagonal2Len = Point2D.distance(points[1].x, points[1].y, points[3].x, points[3].y);
        return NumberUtil.between(diagonal1Len, diagonal2Len * 0.95, diagonal2Len * 1.05);
    }

    //maybe it is better to not use boundingRect and do this on our rect?
    private boolean isAspectRatioCorrect(MatOfPoint matOfPoint) {
        Rect rectangle = Imgproc.boundingRect(matOfPoint);
        double aspectRatio = rectangle.width / (double)rectangle.height;
        return NumberUtil.between(aspectRatio, MIN_ASPECT_RATIO, MAX_ASPECT_RATIO);
    }


}
