package cea.evaluation.model;

import java.util.List;

public class CEABaseline {

    private String filePath;
    private List<SlideTransition> slideTransitions;

    public CEABaseline() {
    }

    public CEABaseline(String filePath, List<SlideTransition> slideTransitions) {
        this.filePath = filePath;
        this.slideTransitions = slideTransitions;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<SlideTransition> getSlideTransitions() {
        return slideTransitions;
    }

    public void setSlideTransitions(List<SlideTransition> slideTransitions) {
        this.slideTransitions = slideTransitions;
    }
}
