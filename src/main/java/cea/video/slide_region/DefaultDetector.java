package cea.video.slide_region;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class DefaultDetector extends SlideRegionDetector {


    protected List<MatOfPoint> findContours(Mat copiedFrame) {
        prepareFrame(copiedFrame);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(copiedFrame, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    private void prepareFrame(Mat frame) {
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame,  new Size(GAUSSIAN_BLUR_KERNEL_SIZE, GAUSSIAN_BLUR_KERNEL_SIZE), 0);
//        Imgproc.adaptiveThreshold(frame, frame, 240, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2);
        Imgproc.threshold(frame, frame, 208, BINARIZATION_INTENSITY_ABOVE_THRESHOLD, Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(MORPHOLOGY_OPEN_KERNEL_SIZE, MORPHOLOGY_OPEN_KERNEL_SIZE));
        Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_CLOSE, kernel);
        kernel.release();
    }



}
