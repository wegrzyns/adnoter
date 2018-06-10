package cea.evaluation.model;

import java.time.Duration;

public class SpeakerUtterance {

    private Duration timestamp;
    private Duration length;
    private String speaker;

    public SpeakerUtterance() {
    }

    public SpeakerUtterance(Duration timestamp, Duration length, String speaker) {
        this.timestamp = timestamp;
        this.length = length;
        this.speaker = speaker;
    }

    public Duration getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Duration timestamp) {
        this.timestamp = timestamp;
    }

    public Duration getLength() {
        return length;
    }

    public void setLength(Duration length) {
        this.length = length;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
