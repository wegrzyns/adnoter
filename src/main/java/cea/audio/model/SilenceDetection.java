package cea.audio.model;

import java.time.Duration;

public class SilenceDetection {

    private Duration silenceStart;
    private Duration silenceDuration;

    public SilenceDetection() {
    }

    public SilenceDetection(Duration silenceStart, Duration silenceDuration) {
        this.silenceStart = silenceStart;
        this.silenceDuration = silenceDuration;
    }

    public Duration getSilenceStart() {
        return silenceStart;
    }

    public void setSilenceStart(Duration silenceStart) {
        this.silenceStart = silenceStart;
    }

    public Duration getSilenceDuration() {
        return silenceDuration;
    }

    public void setSilenceDuration(Duration silenceDuration) {
        this.silenceDuration = silenceDuration;
    }
}
