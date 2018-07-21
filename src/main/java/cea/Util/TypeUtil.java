package cea.Util;

import cea.audio.model.CEASpeakerSegment;
import cea.audio.model.DiarizationResult;
import cea.output.AnnotationResult;
import fr.lium.spkDiarization.system.Diarization;
import org.opencv.core.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeUtil {

    public static MatOfPoint2f convertMatToPoint2f(MatOfPoint matOfPoint) {
        MatOfPoint2f point2f = new MatOfPoint2f();
        matOfPoint.convertTo(point2f, CvType.CV_32F);
        return point2f;
    }

    public static MatOfPoint convertPoint2fToMat(MatOfPoint2f matOfPoint2f) {
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint2f.convertTo(matOfPoint, CvType.CV_32S);
        return matOfPoint;
    }

    public static Mat convertPointMatToMat(MatOfPoint matOfPoint) {
        Mat toRet = new Mat();
        matOfPoint.convertTo(toRet, CvType.CV_32S);
        return toRet;
    }

    public static MatOfPoint convertRectToMatOfPoint(Rect rect) {
        return new MatOfPoint(
                rect.tl(),
                new Point(rect.tl().x, rect.tl().y + rect.height),
                rect.br(),
                new Point(rect.tl().x + rect.width, rect.tl().y));
    }

    public static DiarizationResult convertAnnotationResultToDiarizationResult(AnnotationResult annotationResult) {
        Map<String, List<CEASpeakerSegment>> utterancesMap = new HashMap<>();

        annotationResult.getSpeakerAnnotations().getUtteranceAnnotations().forEach(utteranceAnnotation -> {
            String speakerLabel = utteranceAnnotation.getSpeakerName();
            Duration timestamp = utteranceAnnotation.getUtteranceStart();
            Duration length = utteranceAnnotation.getUtteranceLength();

            if(!utterancesMap.containsKey(speakerLabel)) {
                utterancesMap.put(speakerLabel, new ArrayList<>());
            }

            utterancesMap.get(speakerLabel).add(new CEASpeakerSegment(timestamp, length));
        });

        return new DiarizationResult(utterancesMap, -1);
    }
}
