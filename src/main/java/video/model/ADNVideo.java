package video.model;

import org.opencv.videoio.VideoCapture;

public class ADNVideo {

    private VideoCapture video;
    private double frameArea;
    private double frameRate;

    public ADNVideo(VideoCapture video, double frameArea, double frameRate) {
        this.video = video;
        this.frameArea = frameArea;
        this.frameRate = frameRate;
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
}
