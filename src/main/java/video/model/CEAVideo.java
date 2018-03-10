package video.model;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class CEAVideo {

    private static Logger logger = LoggerFactory.getLogger(CEAVideo.class);

    private VideoCapture video;
    private double frameArea;
    private double frameRate;
    private long frameCount;

    public CEAVideo(VideoCapture video, double frameArea, double frameRate, long frameCount) {
        this.video = video;
        this.frameArea = frameArea;
        this.frameRate = frameRate;
        this.frameCount = frameCount;
    }

    public CEAVideo() {

    }

    public synchronized CEAFrame getFrame(long frameNumber) {
        return new CEAFrame(frame(frameNumber), frameNumber, timeStamp(frameNumber), this);
    }

    private Mat frame(long frameNumber) {
        Mat frame = new Mat();
        setNextFramePosition(frameNumber);
        //TODO: better of this case
        if (!video.read(frame)) logger.info(String.format("Frame %d not found in video", frameNumber));
        return frame;
    }

    private Duration timeStamp(long frameNumber) {
        setNextFramePosition(frameNumber);
        long timeStampMilis = (long) video.get(Videoio.CV_CAP_PROP_POS_MSEC);
        return Duration.ofMillis(timeStampMilis);
    }

    private void setNextFramePosition(long frameNumber) {
        video.set(Videoio.CV_CAP_PROP_POS_FRAMES, frameNumber);
    }

    public VideoCapture getVideo() {
        return video;
    }

    public void setVideo(VideoCapture video) {
        this.video = video;
    }

    public double getFrameArea() {
        return frameArea;
    }

    public void setFrameArea(double frameArea) {
        this.frameArea = frameArea;
    }

    public double getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public long getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(long frameCount) {
        this.frameCount = frameCount;
    }
}
