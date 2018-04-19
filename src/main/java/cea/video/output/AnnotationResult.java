package cea.video.output;

import java.util.List;

public class AnnotationResult {

    private String fileName;
    private List<SlideAnnotation> slideAnnotations;

    public AnnotationResult() {
    }

    public AnnotationResult(String fileName, List<SlideAnnotation> slideAnnotations) {
        this.fileName = fileName;
        this.slideAnnotations = slideAnnotations;
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
}
