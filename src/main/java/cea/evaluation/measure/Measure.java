package cea.evaluation.measure;

import cea.video.model.Detection;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class Measure {

    public static final int CORRECT_SLIDE_CHANGE_MATCH_THRESHOLD_MILLISECONDS = 1000;
    private final int truePositives;
    private final int detectionSize;
    private final int baselineSize;

    public Measure(List<Detection> detections, List<Duration> baseline) {
        truePositives = truePositives(detections, baseline);
        detectionSize = detections.size();
        baselineSize = baseline.size();
    }

    public double precision() {
        return truePositives / (double) detectionSize;
    }

    public double recall() {
        return truePositives / (double) baselineSize;
    }

    public double fMeasure() {
        return 2 * ( (precision() * recall()) / (precision() + recall()) );
    }


    public static boolean timestampMatch(long milis1, long milis2) {
        //TODO: constant to configuration
        return Math.abs(milis1 - milis2) < CORRECT_SLIDE_CHANGE_MATCH_THRESHOLD_MILLISECONDS;
    }

    public static int truePositives(List<Detection> detections, List<Duration> baseline) {
        int truePositives = 0;

        List<Duration> groundTruth = baseline.stream().sorted().collect(Collectors.toList());
        List<Duration> detection = detections.stream().sorted().map(d -> d.getFrame().getTimestamp()).collect(Collectors.toList());

        int i = 0;
        int j = 0;
        long milisDetection;
        long milisGroundTruth;

        for(; i < detection.size() && j < groundTruth.size(); i++ ) {
            milisDetection = detection.get(i).toMillis();
            while(j < groundTruth.size()) {
                milisGroundTruth = groundTruth.get(j).toMillis();
                if(!timestampMatch(milisDetection, milisGroundTruth) )  {
                    if(milisDetection > milisGroundTruth) {
                        j++;
                    }
                    else {
                        break;
                    }
                }
                else {
                    truePositives++;
                    j++;
                    break;
                }
            }
        }

        return truePositives;
    }
}
