package cea.output;

import cea.audio.model.SilenceDetectionResult;
import cea.video.model.BlackFramesDetectionResult;

import java.util.List;

public class AnnotationResult {

    private String fileName;
    private List<SlideAnnotation> slideAnnotations;
    private SpeakerAnnotation speakerAnnotations;
    private SilenceDetectionResult silenceAnnotations;
    private BlackFramesDetectionResult blackFramesDetectionResult;

    public AnnotationResult() {
    }

    public AnnotationResult(String fileName, List<SlideAnnotation> slideAnnotations, SpeakerAnnotation speakerAnnotations, SilenceDetectionResult silenceAnnotations, BlackFramesDetectionResult blackFramesDetectionResult) {
        this.fileName = fileName;
        this.slideAnnotations = slideAnnotations;
        this.speakerAnnotations = speakerAnnotations;
        this.silenceAnnotations = silenceAnnotations;
        this.blackFramesDetectionResult = blackFramesDetectionResult;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<SlideAnnotation> getSlideAnnotations() {
        return slideAnnotations;
    }

    public void setSlideAnnotations(List<SlideAnnotation> slideAnnotations) {
        this.slideAnnotations = slideAnnotations;
    }

    public SpeakerAnnotation getSpeakerAnnotations() {
        return speakerAnnotations;
    }

    public void setSpeakerAnnotations(SpeakerAnnotation speakerAnnotations) {
        this.speakerAnnotations = speakerAnnotations;
    }

    public SilenceDetectionResult getSilenceAnnotations() {
        return silenceAnnotations;
    }

    public void setSilenceAnnotations(SilenceDetectionResult silenceAnnotations) {
        this.silenceAnnotations = silenceAnnotations;
    }

    public BlackFramesDetectionResult getBlackFramesDetectionResult() {
        return blackFramesDetectionResult;
    }

    public void setBlackFramesDetectionResult(BlackFramesDetectionResult blackFramesDetectionResult) {
        this.blackFramesDetectionResult = blackFramesDetectionResult;
    }
}
