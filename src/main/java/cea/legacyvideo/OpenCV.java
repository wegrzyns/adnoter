package cea.legacyvideo;

import cea.Util.GenericUtil;
import cea.Util.NumberUtil;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.xfeatures2d.SIFT;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OpenCV {
    private static double STANDARD_ASPECT_RATIO = 4.0/3.0;
    private static double MIN_ASPECT_RATIO = STANDARD_ASPECT_RATIO * 0.70;
    private static double MAX_ASPECT_RATIO = STANDARD_ASPECT_RATIO * 1.30;
    private static int CHUNK_SIZE_SECONDS = 30;

    private VideoCapture video;
    private double frameArea;
    private SIFT sift;
    private double frameRate;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void open(String fileName) {
        video = new VideoCapture(fileName);
        frameArea = video.get(Videoio.CV_CAP_PROP_FRAME_WIDTH) * video.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT);
//        VideoWriter resultVideo = new VideoWriter("file.avi", VideoWriter.fourcc('M', 'P', 'E', 'G'), 29.88, new Size(1280, 720));
        if(!video.isOpened()) {
            System.out.println("NOT OPEN");
        }
        else {
            frameRate = video.get(Videoio.CV_CAP_PROP_FPS);
            initSIFT();
            Mat originalframe = getNextFrame();

            List<AdnoterFrame> frames = new ArrayList<>();
            int i = 0;
            //while((originalframe = getNextFrame()) != null) {
                frames.add(new AdnoterFrame(originalframe, originalframe.clone()));
                IntStream.rangeClosed(0, 100).forEach(sth ->video.grab());
                originalframe = getNextFrame();
                Imgproc.resize(originalframe, originalframe, new Size(originalframe.width()/2, originalframe.height()/2));
                frames.add(new AdnoterFrame(originalframe, originalframe.clone()));
                prepareFrames(frames);
                processFrames(frames);
//                resultVideo.write(originalframe);
                System.out.println(i++);
            //}
            //resultVideo.release();
        }
//        getChunkedVideo();
    }

    private List<AdnoterChunk> getChunkedVideo() {
        List<AdnoterChunk> toRet = new ArrayList<>();
        AdnoterChunk chunk;
        int frameCount = (int) video.get(Videoio.CV_CAP_PROP_FRAME_COUNT);

        double step = frameRate * CHUNK_SIZE_SECONDS;

        for(int i = 0; i <= frameCount; i+=step) {
            chunk = new AdnoterChunk();
            chunk.setFrameFirst(new AdnFrame(getFrame(i), i));
            chunk.setFrameMiddle(new AdnFrame(getFrame((int) (i+step/2)), (int) (i+step/2)));
            chunk.setFrameLast(new AdnFrame(getFrame((int) (i+step)), (int) (i+step)));
            toRet.add(chunk);
        }

        return toRet;
    }

    private Mat getFrame(int frameNumber) {
        Mat frame = new Mat();
        video.set(Videoio.CV_CAP_PROP_POS_FRAMES, frameNumber);
        video.read(frame);
        return frame;
    }


    private void initSIFT() {
        sift = SIFT.create();
    }

    private void processFrames(List<AdnoterFrame> frames) {
        List<AdnoterFeatures> siftFeatures = new ArrayList<>();
//        List<MatOfDMatch> descriptorMatches = new ArrayList<>();
        MatOfDMatch descriptorMatches = new MatOfDMatch();
        for(AdnoterFrame frame: frames) {
            siftFeatures.add(processFrame(frame.getModifiedFrame(), frame.getOriginalFrame()));
        }

        //TODO: REFACTORRRRRRRRRRRRRRRRRR!!!!!!!!!!!! ENTIRE CLASS, especially this voting here
        if(siftFeatures.get(1).getKeyPoints() == null) {
            siftFeatures.set(1, processFrame(frames.get(1).getModifiedFrame2(), frames.get(1).getOriginalFrame()));
        }

        if(siftFeatures.get(0).getKeyPoints() == null) {
            siftFeatures.set(0, processFrame(frames.get(0).getModifiedFrame2(), frames.get(0).getOriginalFrame()));
        }


        BFMatcher bfMatcher = BFMatcher.create(BFMatcher.BRUTEFORCE, true);
        bfMatcher.match(siftFeatures.get(0).getDescriptors(), siftFeatures.get(1).getDescriptors(), descriptorMatches);
//        List<MatOfDMatch> matches = descriptorMatches.stream()
//                .filter(descriptorMatch -> {
//                    List<DMatch> dMatches = descriptorMatch.toList();
//                    return dMatches.get(0).distance < dMatches.get(1).distance*0.75;
//                })
//                .collect(Collectors.toList());
        Mat resultImage = new Mat();
        if(descriptorMatches.toList().size() < 100) {
            System.out.println("Slide changed!");
        }
//        descriptorMatches.fromList(descriptorMatches.toList().subList(0, 15));
        if(!descriptorMatches.toList().isEmpty()) {
            Features2d.drawMatches(frames.get(0).getOriginalFrame(), siftFeatures.get(0).getKeyPoints(), frames.get(1).getOriginalFrame(), siftFeatures.get(1).getKeyPoints(), descriptorMatches, resultImage);
            showResult(resultImage);
        }


    }

    private AdnoterFeatures processFrame(Mat frame, Mat originalFrame) {
        List<MatOfPoint> contours = new ArrayList<>();
        List<MatOfPoint> slideRegion = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat keyPointsDescriptors = new Mat();
        MatOfKeyPoint siftFeatures = null;
        Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        slideRegion.add(findSlideRegion(convertMatListToPoint2f(contours)));

        if(slideRegion.get(0) != null) {
            Mat frameForSift = new Mat();
            Mat slideRegionMask = prepareMask(slideRegion.get(0), originalFrame);

            Imgproc.cvtColor(originalFrame, frameForSift, Imgproc.COLOR_RGB2GRAY);
            siftFeatures = detectFeatures(frameForSift, slideRegionMask);
            sift.compute(frameForSift, siftFeatures, keyPointsDescriptors);
            drawContoursAndKeypoints(originalFrame, false, slideRegion, siftFeatures);
        }


        showResult(originalFrame);


        return new AdnoterFeatures(keyPointsDescriptors, siftFeatures);
    }

    private void drawContoursAndKeypoints(Mat targetFrame, boolean greyscale, List<MatOfPoint> contours, MatOfKeyPoint siftFeatures) {
        if(greyscale) {
            Imgproc.cvtColor(targetFrame, targetFrame, Imgproc.COLOR_GRAY2RGB);
        }
        Imgproc.drawContours(targetFrame, contours, -1, new Scalar(0, 0, 255), 3);
        if(siftFeatures != null) {
            Features2d.drawKeypoints(targetFrame, siftFeatures, targetFrame, new Scalar(0, 255, 0), Features2d.DRAW_RICH_KEYPOINTS);
        }

    }

    private Mat prepareMask(MatOfPoint slideRegion, Mat originalFrame) {
        Mat slideRegionMask = Mat.zeros(originalFrame.rows(), originalFrame.cols(), CvType.CV_8UC(1));
        Imgproc.fillConvexPoly(slideRegionMask, slideRegion, new Scalar(1));
        return slideRegionMask;
    }

    private MatOfKeyPoint detectFeatures(Mat frame, Mat frameRegionMask) {
        MatOfKeyPoint toRet = new MatOfKeyPoint();
        sift.detect(frame, toRet, frameRegionMask);
        return toRet;
    }

    private List<MatOfPoint2f> convertMatListToPoint2f(List<MatOfPoint> matOfPoints) {
        return matOfPoints.stream().map(mat -> {
            MatOfPoint2f point2f = new MatOfPoint2f();
            mat.convertTo(point2f, CvType.CV_32F);
            return point2f;
        }).collect(Collectors.toList());
    }

    private void prepareFrames(List<AdnoterFrame> frames) {
        Mat frame;
        for(AdnoterFrame adnoterFrame: frames) {
            frame = adnoterFrame.getModifiedFrame();
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
            Imgproc.GaussianBlur(frame, frame, new Size(7, 7), 0);
            adnoterFrame.setModifiedFrame2(frame.clone());
//            Imgproc.threshold(frame, frame, 200, 240, Imgproc.THRESH_TRIANGLE);
            //TODO: proper voting refactor prolly: OTSU + ADAPTIVE MEAN/GAUSS + TRIANGLE
            Imgproc.threshold(adnoterFrame.getModifiedFrame2(), adnoterFrame.getModifiedFrame2(), 200, 240, Imgproc.THRESH_OTSU);
            Imgproc.adaptiveThreshold(frame, frame, 240, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2);
            Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(9, 9));
            Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(adnoterFrame.getModifiedFrame2(), adnoterFrame.getModifiedFrame2(), Imgproc.MORPH_OPEN, kernel);
        }
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
        Mat contour = new Mat();
        matOfPoint.convertTo(contour, CvType.CV_32S);
        if(Imgproc.contourArea(contour) < (frameArea/14)) {
            return false;
        }
        if(!isRectangle(matOfPoint)) {
            return false;
        }
        Rect rectangle = Imgproc.boundingRect(matOfPoint);
        double aspectRatio = rectangle.width / (double)rectangle.height;
        return NumberUtil.between(aspectRatio, MIN_ASPECT_RATIO, MAX_ASPECT_RATIO);
    }

    //checks for diagonal equality, this is does not guarantee rectangle but close enough?
    private boolean isRectangle(MatOfPoint rectangleVertices) {
        Point[] points = rectangleVertices.toArray();
        double diagonal1Len = Point2D.distance(points[0].x, points[0].y, points[2].x, points[2].y);
        double diagonal2Len = Point2D.distance(points[1].x, points[1].y, points[3].x, points[3].y);
        return NumberUtil.between(diagonal1Len, diagonal2Len * 0.95, diagonal2Len * 1.05);
    }

    private boolean isQuadrangle(MatOfPoint matOfPoint) {
        return matOfPoint.toList().size() == 4;
    }

    public static void showResult(Mat img) {
//        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();

        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage;
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
