package video;

import Util.GenericUtil;
import Util.NumberUtil;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OpenCV {
    private static double STANDARD_ASPECT_RATIO = 4.0/3.0;
    private static double MIN_ASPECT_RATIO = STANDARD_ASPECT_RATIO * 0.95;
    private static double MAX_ASPECT_RATIO = STANDARD_ASPECT_RATIO * 1.05;
    private VideoCapture video;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void open(String fileName) {
        video = new VideoCapture(fileName);
        VideoWriter resultVideo = new VideoWriter("file.avi", VideoWriter.fourcc('M', 'P', 'E', 'G'), 29.88, new Size(1920, 540));
        if(!video.isOpened()) {
            System.out.println("NOT OPEN");
        }
        else {
            Mat originalframe;
            Mat frame;
            while((originalframe = getNextFrame()) != null) {
                frame = originalframe.clone();
                prepareFrame(frame);
                processFrame(frame, originalframe);
                resultVideo.write(originalframe);
            }
        }

    }

    private void processFrame(Mat frame, Mat originalFrame) {
        List<MatOfPoint> contours = new ArrayList<>();
        List<MatOfPoint> slideRegion = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        slideRegion.add(findSlideRegion(convertMatListToPoint2f(contours)));
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2RGB);
        if(slideRegion.get(0) != null) {
            Imgproc.drawContours(originalFrame, slideRegion, -1, new Scalar(0, 0, 255), 3);
        }
//        showResult(originalFrame);
    }

    private List<MatOfPoint2f> convertMatListToPoint2f(List<MatOfPoint> matOfPoints) {
        return matOfPoints.stream().map(mat -> {
            MatOfPoint2f point2f = new MatOfPoint2f();
            mat.convertTo(point2f, CvType.CV_32F);
            return point2f;
        }).collect(Collectors.toList());
    }

    private void prepareFrame(Mat frame) {
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame, new Size(7, 7), 0);
        Imgproc.threshold(frame, frame, 200, 240, Imgproc.THRESH_OTSU);
//            Imgproc.adaptiveThreshold(frame, frame, 240, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(5, 5));
        Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_OPEN, kernel);
    }

    private Mat getNextFrame() {
        Mat frame = new Mat();
        if(video.read(frame)) {
            return frame;
        }
        return null;
    }

    private MatOfPoint findSlideRegion(List<MatOfPoint2f> contours){
        double contourLength;
        MatOfPoint formatedContour;
        List<MatOfPoint> potentialSlideRegions = new ArrayList<>();

        for(MatOfPoint2f contour: contours) {
            contourLength = Imgproc.arcLength(contour, true);
            Imgproc.approxPolyDP(contour, contour, contourLength*0.05, true);

            formatedContour = new MatOfPoint();
            contour.convertTo(formatedContour, CvType.CV_32S);
            if(isPotentialSlideRegion(formatedContour)) {
                potentialSlideRegions.add(formatedContour);
            }
        }
        return potentialSlideRegions.stream()
                .max(Comparator.comparing(GenericUtil.cache(Imgproc::contourArea)))
                .orElse(null);
    }

    private boolean isPotentialSlideRegion(MatOfPoint matOfPoint) {
        if(!isQuadrangle(matOfPoint)) {
            return false;
        }
        Rect rectangle = Imgproc.boundingRect(matOfPoint);
        double aspectRatio = rectangle.width / (double)rectangle.height;
        if(NumberUtil.between(aspectRatio, MIN_ASPECT_RATIO, MAX_ASPECT_RATIO)) {
            return true;
        }
        return false;
    }

    private boolean isQuadrangle(MatOfPoint matOfPoint) {
        return matOfPoint.toList().size() == 4;
    }

    public static void showResult(Mat img) {
//        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();

        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
