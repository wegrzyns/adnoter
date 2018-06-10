package cea.evaluation.measure;

import cea.audio.model.CEASpeakerSegment;
import cea.audio.model.DiarizationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DiarizationMeasure {

    private DiarizationResult diarizationResult;
    private Map<String, List<CEASpeakerSegment>> baselineUtterances;

    public DiarizationMeasure() {
    }

    public DiarizationMeasure(DiarizationResult diarizationResult, Map<String, List<CEASpeakerSegment>> baselineUtterances) {
        this.diarizationResult = diarizationResult;
        this.baselineUtterances = baselineUtterances;
    }

    public double diarizationErrorRate() {
        List<DiarizationMatch> bestMappings = bestMappings();

        return 0;
    }

    private List<DiarizationMatch> bestMappings() {
        int baselineSpeakerCount = baselineUtterances.keySet().size();
        int hypothesisSpeakerCount = diarizationResult.getUtterances().keySet().size();

        DiarizationMatch[][] matches = new DiarizationMatch[baselineSpeakerCount][hypothesisSpeakerCount];
        double[][] costMatrix = new double[baselineSpeakerCount][hypothesisSpeakerCount];

        for(int i = 0; i < baselineSpeakerCount; i++) {
            for(int j = 0; j < hypothesisSpeakerCount; j++) {
                matches[i][j] = createDiarizationMatch(i, j);
                costMatrix[i][j] = matches[i][j].getNonMatchedTime();
            }
        }

        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(costMatrix);
        int[] bestMatches = hungarianAlgorithm.execute();

        List<DiarizationMatch> bestDiarizationMatches = new ArrayList<>();
        for(int i = 0; i < bestMatches.length; i++) {
            bestDiarizationMatches.add(matches[i][bestMatches[i]]);
        }

        return bestDiarizationMatches;
    }

    private DiarizationMatch createDiarizationMatch(int i, int j) {
        String baselineSpeakerName = (String) baselineUtterances.keySet().toArray()[i];
        String hypothesisSpeakerName = (String) diarizationResult.getUtterances().keySet().toArray()[j];

        List<CEASpeakerSegment> baselineSpeakerSegments = baselineUtterances.get(baselineSpeakerName);
        List<CEASpeakerSegment> hypothesisSpeakerSegments = diarizationResult.getUtterances().get(hypothesisSpeakerName);


        double baselineSpeakerSegmentsLength = baselineUtterances.values().stream().mapToLong(baselineSpeakerSegment -> baselineSpeakerSegment.stream().mapToLong(a -> a.getLength().getSeconds()).reduce(0L, (a, b) -> a + b)).reduce(0L, (a, b) -> a + b);
        double diarizationErrorRateTime = 0;


        for(CEASpeakerSegment baselineSpeakerSegs: baselineSpeakerSegments) {
            for(CEASpeakerSegment hypothesisSpeakerSegs: hypothesisSpeakerSegments) {
                diarizationErrorRateTime += nonMatchedTime(baselineSpeakerSegs, hypothesisSpeakerSegs);
            }
        }

        return new DiarizationMatch(baselineSpeakerName, hypothesisSpeakerName, diarizationErrorRateTime/baselineSpeakerSegmentsLength);
    }

    private double nonMatchedTime(CEASpeakerSegment baselineSegment, CEASpeakerSegment hypothesisSegment) {
        //TODO: fix calculations in this method, incorrect metric, should return full DER not un matched time!!!
        long baselineStartSecond = baselineSegment.getTimestamp().getSeconds();
        long hypothesisStartSecond = hypothesisSegment.getTimestamp().getSeconds();

        long baselineEndSecond = baselineStartSecond + baselineSegment.getLength().getSeconds();
        long hypothesisEndSecond = hypothesisStartSecond + hypothesisSegment.getLength().getSeconds();

        long baselineSegmentLength = baselineSegment.getLength().getSeconds();
        long hypothesisSegmentLength = hypothesisSegment.getLength().getSeconds();

        if(isOverlapping(baselineStartSecond, hypothesisStartSecond, baselineEndSecond, hypothesisEndSecond)) {
            List<Long> values = new ArrayList<>();

            values.add(baselineSegment.getLength().getSeconds());
            values.add(baselineEndSecond - hypothesisStartSecond);
            values.add(hypothesisEndSecond - baselineStartSecond);
            values.add(hypothesisSegment.getLength().getSeconds());


            long overlapLength = values.stream().min(Comparator.comparing(Long::intValue)).get();

            return Math.max(hypothesisSegmentLength, baselineSegmentLength) - overlapLength;
        } else {
            return Math.max(hypothesisSegmentLength, baselineSegmentLength);
        }
    }

    private boolean isOverlapping(long baselineStartSecond, long hypothesisStartSecond, long baselineEndSecond, long hypothesisEndSecond) {
        return Math.max(baselineStartSecond, hypothesisStartSecond) <= Math.min(baselineEndSecond, hypothesisEndSecond);
    }
}
