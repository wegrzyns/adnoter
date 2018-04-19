package cea.video.output;

import cea.video.model.Detection;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.opencv.core.Point;

import java.time.Duration;
import java.util.List;

public class SlideAnnotation {

    private AnnotationType type;
    private Duration timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Point> slideRegionVertices;

    public SlideAnnotation() {
    }

    public SlideAnnotation(AnnotationType type, Duration timestamp, List<Point> slideRegionVertices) {
        this.type = type;
        this.timestamp = timestamp;
        this.slideRegionVertices = slideRegionVertices;
    }

    public SlideAnnotation(AnnotationType type, Detection detection) {
        this.type = type;
        this.timestamp = detection.getFrame().getTimestamp();
        this.slideRegionVertices = detection.getFrame().getSlideRegionVertices();
    }

    public SlideAnnotation(AnnotationType type) {
        this.type = type;
    }

    public AnnotationType getType() {
        return type;
    }

    public void setType(AnnotationType type) {
        this.type = type;
    }

    public Duration getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Duration timestamp) {
        this.timestamp = timestamp;
    }

    public List<Point> getSlideRegionVertices() {
        return slideRegionVertices;
    }

    public void setSlideRegionVertices(List<Point> slideRegionVertices) {
        this.slideRegionVertices = slideRegionVertices;
    }
}
