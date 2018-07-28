package cea.output;

import cea.Util.JsonUtil;
import cea.audio.model.DiarizationResult;
import cea.video.model.Detection;
import cea.video.model.DetectionType;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Output {

    public static void createOutputAnnotation(DiarizationResult diarizationResult, List<Detection> detections, String fileName) throws IOException {
        List<SlideAnnotation> slideAnnotations = new ArrayList<>();
        List<UtteranceAnnotation> utteranceAnnotations = new ArrayList<>();
        boolean slideRegionVisible = false;
        SlideAnnotation slideAnnotation = null;
        AnnotationResult annotationResult;

        for(Detection detection: detections) {

            if(detection.getType() == DetectionType.SLIDE_REGION_TOGGLE) {
                if(!slideRegionVisible) {
                    slideAnnotation = new SlideAnnotation(SlideAnnotationType.SLIDE_REGION_IN, detection);
                }
                else {
                    slideAnnotation = new SlideAnnotation(SlideAnnotationType.SLIDE_REGION_OUT, detection);
                }
                slideRegionVisible = !slideRegionVisible;
            }
            else if(detection.getType() == DetectionType.SLIDE_CHANGE) {
                slideAnnotation = new SlideAnnotation(SlideAnnotationType.SLIDE_TRANSITION, detection);
            }

            if(slideAnnotation != null) {
                slideAnnotations.add(slideAnnotation);
            }
        }

        diarizationResult.getUtterances().forEach((speakerName, speakerUtterances) -> speakerUtterances.forEach(utterance -> {
            Duration utteranceStart = utterance.getTimestamp();
            Duration utteranceLength = utterance.getLength();
            utteranceAnnotations.add(new UtteranceAnnotation(speakerName, utteranceStart, utteranceLength));
        }));

        SpeakerAnnotation speakerAnnotation = new SpeakerAnnotation(utteranceAnnotations, diarizationResult.getSpeakerCount());

        annotationResult = new AnnotationResult(fileName, slideAnnotations, speakerAnnotation);
        JsonUtil.annotationResultToJson(annotationResult);
    }
}
