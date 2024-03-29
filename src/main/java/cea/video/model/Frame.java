package cea.video.model;

import cea.video.frame_similarity.feature.Feature;
import cea.video.frame_similarity.feature.FeatureType;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frame implements AutoCloseable {

    private Mat frame;
    private long position;
    private Duration timestamp;
    private Video video;
    private Map<FeatureType, Boolean> isFeatureComputed;
    private Map<FeatureType, Feature> features;
    private boolean slideRegionDetected;
    private List<Point> slideRegionVertices;

    public Frame(Mat frame, long position, Duration timestamp, Video video) {
        this.frame = frame;
        this.position = position;
        this.timestamp = timestamp;
        this.video = video;
        this.isFeatureComputed = new HashMap<>();
        this.features = new HashMap<>();
        for(FeatureType featureType: FeatureType.values()) {
            isFeatureComputed.put(featureType, false);
        }
        this.slideRegionDetected = false;
    }

    public synchronized Mat getFrame() {
        if(frame == null || frame.empty()) {
            this.frame = video.frame(position);
            this.timestamp = video.timeStamp(position);
        }
        return frame;
    }

    public void setFrame(Mat frame) {
        this.frame = frame;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public Duration getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Duration timestamp) {
        this.timestamp = timestamp;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public boolean isFeatureComputed(FeatureType featureType) {
        return isFeatureComputed.get(featureType);
    }

    public void setFeatureComputed(FeatureType featureType) {
        isFeatureComputed.replace(featureType,false, true);
    }

    public Feature getFeature(FeatureType featureType) {
        return features.get(featureType);
    }

    public synchronized void addFeature(FeatureType featureType, Feature feature) {
        features.put(featureType, feature);
    }

    public void setSlideRegionDetected() {
        this.slideRegionDetected = true;
    }

    public boolean isSlideRegionDetected() {
        return slideRegionDetected;
    }

    public List<Point> getSlideRegionVertices() {
        return slideRegionVertices;
    }

    public void setSlideRegionVertices(List<Point> slideRegionVertices) {
        this.slideRegionVertices = slideRegionVertices;
    }

    @Override
    public String toString() {
        return "Frame{" +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public void close() throws Exception {
        frame.release();
    }
}
