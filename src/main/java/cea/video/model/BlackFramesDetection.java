package cea.video.model;

import java.time.Duration;

public class BlackFramesDetection {

    private Duration timestamp;

    public BlackFramesDetection() {
    }

    public BlackFramesDetection(Duration timestamp) {
        this.timestamp = timestamp;
    }

    public Duration getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Duration timestamp) {
        this.timestamp = timestamp;
    }

}
