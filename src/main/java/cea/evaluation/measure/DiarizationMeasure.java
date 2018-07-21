package cea.evaluation.measure;

import cea.audio.model.CEASpeakerSegment;
import cea.audio.model.DiarizationResult;

import java.util.*;

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

        return bestMappings.stream().mapToDouble(DiarizationMatch::getDiarizationErrorRate).sum();
    }

    private List<DiarizationMatch> bestMappings() {
        int baselineSpeakerCount = baselineUtterances.keySet().size();
        int hypothesisSpeakerCount = diarizationResult.getUtterances().keySet().size();

        DiarizationMatch[][] matches = new DiarizationMatch[baselineSpeakerCount][hypothesisSpeakerCount];
        double[][] costMatrix = new double[baselineSpeakerCount][hypothesisSpeakerCount];

        for(int i = 0; i < baselineSpeakerCount; i++) {
            for(int j = 0; j < hypothesisSpeakerCount; j++) {
                matches[i][j] = createDiarizationMatch(i, j);
                costMatrix[i][j] = matches[i][j].getDiarizationErrorRate();
            }
        }

        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(costMatrix);
        int[] bestMatches = hungarianAlgorithm.execute();

        List<DiarizationMatch> bestDiarizationMatches = new ArrayList<>();
        for(int i = 0; i < bestMatches.length; i++) {
            if (bestMatches[i] > -1) {
                bestDiarizationMatches.add(matches[i][bestMatches[i]]);
            }
        }

        return bestDiarizationMatches;
    }

    private DiarizationMatch createDiarizationMatch(int i, int j) {
        String baselineSpeakerName = (String) baselineUtterances.keySet().toArray()[i];
        String hypothesisSpeakerName = (String) diarizationResult.getUtterances().keySet().toArray()[j];

        List<CEASpeakerSegment> baselineSpeakerSegments = baselineUtterances.get(baselineSpeakerName);
        List<CEASpeakerSegment> hypothesisSpeakerSegments = diarizationResult.getUtterances().get(hypothesisSpeakerName);

        List<CEASpeakerSegment> allOtherHypothesisSpeakerSegments = new ArrayList<>();

        diarizationResult.getUtterances().entrySet().stream()
                .filter(entry -> !Objects.equals(entry.getKey(), hypothesisSpeakerName))
                .forEach(entry -> allOtherHypothesisSpeakerSegments.addAll(entry.getValue()));

        List<CEASpeakerSegment> allSegments = new ArrayList<>();
        allSegments.addAll(baselineSpeakerSegments);
        allSegments.addAll(hypothesisSpeakerSegments);

        Collections.sort(baselineSpeakerSegments);
        Collections.sort(hypothesisSpeakerSegments);

        double allBaselineSpeakerSegmentsLength = baselineUtterances.values().stream().mapToLong(baselineSpeakerSegment -> baselineSpeakerSegment.stream().mapToLong(a -> a.getLength().getSeconds()).reduce(0L, (a, b) -> a + b)).reduce(0L, (a, b) -> a + b);

        long matchingStart = Math.min(baselineSpeakerSegments.get(0).getTimestamp().getSeconds(),hypothesisSpeakerSegments.get(0).getTimestamp().getSeconds());
        long matchingEnd = allSegments.stream().mapToLong(ceaSpeakerSegment -> ceaSpeakerSegment.getTimestamp().getSeconds() + ceaSpeakerSegment.getLength().getSeconds()).max().getAsLong();

        long correctlyMatchedSeconds = 0;
        long missedDetectionSeconds = 0;
        long falseAlarmSeconds = 0;
        long confusedSpeakerSeconds = 0;

        for(long second = matchingStart; second < matchingEnd; second++) {
            if(isSecondInSegments(second, baselineSpeakerSegments) && isSecondInSegments(second, hypothesisSpeakerSegments)) {
                correctlyMatchedSeconds++;
            }
            if(isSecondInSegments(second, baselineSpeakerSegments) && isSecondInSegments(second, allOtherHypothesisSpeakerSegments)) {
                confusedSpeakerSeconds++;
            }
            if(!isSecondInSegments(second, baselineSpeakerSegments) && isSecondInSegments(second, hypothesisSpeakerSegments)) {
                falseAlarmSeconds++;
            }
            if(isSecondInSegments(second, baselineSpeakerSegments) && !isSecondInSegments(second, hypothesisSpeakerSegments)) {
                missedDetectionSeconds++;
            }
        }

        double DERTimeInSeconds = confusedSpeakerSeconds + missedDetectionSeconds + falseAlarmSeconds;

        return new DiarizationMatch(baselineSpeakerName, hypothesisSpeakerName, DERTimeInSeconds/allBaselineSpeakerSegmentsLength);
    }

    private boolean isSecondInSegments(long second, List<CEASpeakerSegment> segments) {
        long segStart;
        long segEnd;
        for(CEASpeakerSegment seg: segments) {
            segStart = seg.getTimestamp().getSeconds();
            segEnd = segStart + seg.getLength().getSeconds();
            if(isOverlapping(segStart, second, segEnd, second + 1)) {
                return true;
            }
        }

        return false;
    }

    private long overallSpeechLengthInList(List<CEASpeakerSegment> segments) {
        return segments.stream()
                .mapToLong(baselineSpeakerSegment -> baselineSpeakerSegment.getLength().getSeconds())
                .reduce(0L, (a, b) -> a + b);
    }

    private boolean isOverlapping(long baselineStartSecond, long hypothesisStartSecond, long baselineEndSecond, long hypothesisEndSecond) {
        return Math.max(baselineStartSecond, hypothesisStartSecond) <= Math.min(baselineEndSecond, hypothesisEndSecond);
    }
}
