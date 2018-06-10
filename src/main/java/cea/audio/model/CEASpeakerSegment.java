package cea.audio.model;

import java.time.Duration;

public class CEASpeakerSegment {

    private Duration timestamp;
    private Duration length;

    public CEASpeakerSegment(Duration timestamp, Duration length) {
        this.timestamp = timestamp;
        this.length = length;
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
}
