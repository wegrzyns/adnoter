package cea.evaluation.model;

import java.time.Duration;

public class SlideTransition {

    private Duration timestamp;
    private SlideTransitionType type;

    public SlideTransition() {
    }

    public SlideTransition(Duration timestamp, SlideTransitionType type) {
        this.timestamp = timestamp;
        this.type = type;
    }

    public Duration getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Duration timestamp) {
        this.timestamp = timestamp;
    }

    public SlideTransitionType getType() {
        return type;
    }

    public void setType(SlideTransitionType type) {
        this.type = type;
    }
}
