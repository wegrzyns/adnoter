package cea.Util;

import org.opencv.core.*;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;


public class ImageDisplayUtil {

    public static void showResult(Mat img, Duration frameTime) {
//        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();

        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.setTitle(frameTime.toString());
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawContoursAndKeypoints(Mat targetFrame, boolean greyscale, List<MatOfPoint> contours, MatOfKeyPoint siftFeatures) {
        if(greyscale) {
            Imgproc.cvtColor(targetFrame, targetFrame, Imgproc.COLOR_GRAY2RGB);
        }
        if(contours != null) {
            Imgproc.drawContours(targetFrame, contours, -1, new Scalar(0, 0, 255), 18);
        }
        if(siftFeatures != null) {
            Features2d.drawKeypoints(targetFrame, siftFeatures, targetFrame, new Scalar(0, 255, 0), Features2d.DRAW_RICH_KEYPOINTS);
        }
    }
}
