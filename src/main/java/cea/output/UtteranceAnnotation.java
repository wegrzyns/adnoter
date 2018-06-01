package cea.output;

import java.time.Duration;

public class UtteranceAnnotation {

    private String speakerName;
    private Duration utteranceStart;
    private Duration utteranceLength;

    public UtteranceAnnotation() {
    }

    public UtteranceAnnotation(String speakerName, Duration utteranceStart, Duration utteranceLength) {
        this.speakerName = speakerName;
        this.utteranceStart = utteranceStart;
        this.utteranceLength = utteranceLength;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public Duration getUtteranceStart() {
        return utteranceStart;
    }

    public void setUtteranceStart(Duration utteranceStart) {
        this.utteranceStart = utteranceStart;
    }

    public Duration getUtteranceLength() {
        return utteranceLength;
    }

    public void setUtteranceLength(Duration utteranceLength) {
        this.utteranceLength = utteranceLength;
    }
}
