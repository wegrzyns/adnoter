package cea.output;

import cea.Util.JsonUtil;
import cea.audio.model.DiarizationResult;
import cea.audio.model.SilenceDetectionResult;
import cea.video.model.BlackFramesDetection;
import cea.video.model.BlackFramesDetectionResult;
import cea.video.model.Detection;
import cea.video.model.DetectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutputManager {

    public static void createOutputAnnotation(DiarizationResult diarizationResult, List<Detection> detections, SilenceDetectionResult silenceDetectionResult, String fileName) throws IOException {
        List<SlideAnnotation> slideAnnotations;
        SpeakerAnnotation speakerAnnotation = null;
        BlackFramesDetectionResult blackFramesDetectionResult;
        AnnotationResult annotationResult;

        blackFramesDetectionResult = handleVideoBlackFramesDetections(detections);
        slideAnnotations = handleVideoTransitionDetections(detections);

        if(diarizationResult != null) {
            speakerAnnotation = diarizationResult.getSpeakerAnnotation();
        }

        annotationResult = new AnnotationResult(fileName, slideAnnotations, speakerAnnotation, silenceDetectionResult, blackFramesDetectionResult);
        JsonUtil.annotationResultToJson(annotationResult);
    }

    private static BlackFramesDetectionResult handleVideoBlackFramesDetections(List<Detection> detections) {

        return new BlackFramesDetectionResult(detections.stream()
                .filter(detection -> detection.getType() == DetectionType.BLACK_FRAMES_TRANSITION)
                .map(detection -> new BlackFramesDetection(detection.getFrame().getTimestamp()))
                .collect(Collectors.toList()));
    }

    private static List<SlideAnnotation> handleVideoTransitionDetections(List<Detection> detections) {
        List<SlideAnnotation> slideAnnotations = new ArrayList<>();
        boolean slideRegionVisible = false;
        SlideAnnotation slideAnnotation = null;

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

        return slideAnnotations;
    }
}
