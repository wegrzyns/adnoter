package Util;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;

public class TypeUtil {

    public static MatOfPoint2f convertMatToPoint2f(MatOfPoint matOfPoint) {
        MatOfPoint2f point2f = new MatOfPoint2f();
        matOfPoint.convertTo(point2f, CvType.CV_32F);
        return point2f;
    }

    public static MatOfPoint convertPoint2fToMat(MatOfPoint2f matOfPoint2f) {
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint2f.convertTo(matOfPoint, CvType.CV_32S);
        return matOfPoint;
    }

    public static Mat convertPointMatToMat(MatOfPoint matOfPoint) {
        Mat toRet = new Mat();
        matOfPoint.convertTo(toRet, CvType.CV_32S);
        return toRet;
    }
}
