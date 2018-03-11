package evaluation.model;

import java.time.Duration;
import java.util.List;

public class CEABaseline {

    private String filePath;
    private List<Duration> slideTransitions;

    public CEABaseline() {
    }

    public CEABaseline(String filePath, List<Duration> slideTransitions) {
        this.filePath = filePath;
        this.slideTransitions = slideTransitions;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Duration> getSlideTransitions() {
        return slideTransitions;
    }

    public void setSlideTransitions(List<Duration> slideTransitions) {
        this.slideTransitions = slideTransitions;
    }
}
