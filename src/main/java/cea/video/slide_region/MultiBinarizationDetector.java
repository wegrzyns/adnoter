package cea.video.slide_region;

import cea.Util.ImageDisplayUtil;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MultiBinarizationDetector extends SlideRegionDetector {

    private static final int MIN_BINARIZATION_THRESHOLD = 16;
    private static final int THRESHOLD_COUNT = 16;

    protected List<MatOfPoint> findContours(Mat copiedFrame) {
        List<MatOfPoint> contours = new ArrayList<>();

        IntStream
                .iterate(MIN_BINARIZATION_THRESHOLD, threshold -> threshold += 256 / THRESHOLD_COUNT)
                .limit(THRESHOLD_COUNT)
                .forEach(threshold -> {
                    Mat preparedFrame = prepareFrame(copiedFrame, threshold);
                    Mat hierarchy = new Mat();
                    Imgproc.findContours(preparedFrame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                    preparedFrame.release();
                    hierarchy.release();
                });

//        Mat frame = copiedFrame.clone();
//        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
////        Imgproc.GaussianBlur(frame, frame,  new Size(GAUSSIAN_BLUR_KERNEL_SIZE, GAUSSIAN_BLUR_KERNEL_SIZE), 0);
//        Imgproc.adaptiveThreshold(frame, frame, BINARIZATION_INTENSITY_ABOVE_THRESHOLD, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 19, 2);
//        ImageDisplayUtil.showResult(frame, Duration.ZERO);
//        Mat hierarchy = new Mat();
//        Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
////        ImageDisplayUtil.drawContoursAndKeypoints(frame, true, contours.subList(contours.size()-2, contours.size()-1), null);
////        ImageDisplayUtil.showResult(frame, Duration.ZERO);
//        hierarchy.release();

        return contours;
    }

    private Mat prepareFrame(Mat originalFrame, int binarizationThreshold) {
        Mat frame = originalFrame.clone();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame,  new Size(GAUSSIAN_BLUR_KERNEL_SIZE, GAUSSIAN_BLUR_KERNEL_SIZE), 0);
        Imgproc.threshold(frame, frame, binarizationThreshold, BINARIZATION_INTENSITY_ABOVE_THRESHOLD, Imgproc.THRESH_BINARY);
//        ImageDisplayUtil.showResult(frame, Duration.ZERO);
//        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(MORPHOLOGY_CLOSE_KERNEL_SIZE, MORPHOLOGY_CLOSE_KERNEL_SIZE));
//        Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_CLOSE, kernel);
//        kernel.release();

        return frame;
    }
}
