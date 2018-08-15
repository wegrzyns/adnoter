package cea.audio.model;

import java.util.List;

public class SilenceDetectionResult {

    private List<SilenceDetection> silenceDetections;

    public SilenceDetectionResult() {
    }

    public SilenceDetectionResult(List<SilenceDetection> silenceDetections) {
        this.silenceDetections = silenceDetections;
    }

    public List<SilenceDetection> getSilenceDetections() {
        return silenceDetections;
    }

    public void setSilenceDetections(List<SilenceDetection> silenceDetections) {
        this.silenceDetections = silenceDetections;
    }
}
