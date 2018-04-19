package cea.video.output;

import cea.Util.JsonUtil;
import cea.video.model.Detection;
import cea.video.model.DetectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoOutput {

    public static void createOutputAnnotation(List<Detection> detections, String fileName) throws IOException {
        List<SlideAnnotation> slideAnnotations = new ArrayList<>();
        boolean slideRegionVisible = false;
        SlideAnnotation slideAnnotation = null;
        AnnotationResult annotationResult;

        for(Detection detection: detections) {

            if(detection.getType() == DetectionType.SLIDE_REGION_TOGGLE) {
                if(!slideRegionVisible) {
                    slideAnnotation = new SlideAnnotation(AnnotationType.SLIDE_REGION_IN, detection);
                }
                else {
                    slideAnnotation = new SlideAnnotation(AnnotationType.SLIDE_REGION_OUT, detection);
                }
                slideRegionVisible = !slideRegionVisible;
            }
            else if(detection.getType() == DetectionType.SLIDE_CHANGE) {
                slideAnnotation = new SlideAnnotation(AnnotationType.SLIDE_TRANSITION, detection);
            }

            if(slideAnnotation != null) {
                slideAnnotations.add(slideAnnotation);
            }
        }

        annotationResult = new AnnotationResult(fileName, slideAnnotations);
        JsonUtil.annotationResultToJson(annotationResult);
    }
}
