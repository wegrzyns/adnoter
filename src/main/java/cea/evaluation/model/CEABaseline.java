package cea.evaluation.model;

import java.util.List;

public class CEABaseline {

    private String filePath;
    private List<SlideTransition> slideTransitions;
    private List<SpeakerUtterance> speakerUtterances;

    public CEABaseline() {
    }

    public CEABaseline(String filePath, List<SlideTransition> slideTransitions, List<SpeakerUtterance> speakerUtterances) {
        this.filePath = filePath;
        this.slideTransitions = slideTransitions;
        this.speakerUtterances = speakerUtterances;
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

    public List<SpeakerUtterance> getSpeakerUtterances() {
        return speakerUtterances;
    }

    public void setSpeakerUtterances(List<SpeakerUtterance> speakerUtterances) {
        this.speakerUtterances = speakerUtterances;
    }
}
