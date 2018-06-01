package cea.output;

import java.util.List;

public class AnnotationResult {

    private String fileName;
    private List<SlideAnnotation> slideAnnotations;
    private SpeakerAnnotation speakerAnnotations;

    public AnnotationResult() {
    }

    public AnnotationResult(String fileName, List<SlideAnnotation> slideAnnotations, SpeakerAnnotation speakerAnnotations) {
        this.fileName = fileName;
        this.slideAnnotations = slideAnnotations;
        this.speakerAnnotations = speakerAnnotations;
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
}
