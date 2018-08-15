package cea.video.model;

import java.util.List;

public class BlackFramesDetectionResult {

    private List<BlackFramesDetection> blackFramesDetections;

    public BlackFramesDetectionResult() {
    }

    public BlackFramesDetectionResult(List<BlackFramesDetection> blackFramesDetections) {
        this.blackFramesDetections = blackFramesDetections;
    }

    public List<BlackFramesDetection> getBlackFramesDetections() {
        return blackFramesDetections;
    }

    public void setBlackFramesDetections(List<BlackFramesDetection> blackFramesDetections) {
        this.blackFramesDetections = blackFramesDetections;
    }

    public void addBlackFrameDetection(BlackFramesDetection blackFramesDetection) {
        this.blackFramesDetections.add(blackFramesDetection);
    }
}
