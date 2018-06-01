package cea.audio.model;

import fr.lium.spkDiarization.libClusteringData.Segment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiarizationResult {

    private Map<String, List<CEASegment>> utterances;
    private double audienceActivity;

    public DiarizationResult() {
        utterances = new HashMap<>();
    }

    public DiarizationResult(Map<String, List<CEASegment>> utterances, double audienceActivity) {
        this.utterances = utterances;
        this.audienceActivity = audienceActivity;
    }

    public Map<String, List<CEASegment>> getUtterances() {
        return utterances;
    }

    public void setUtterances(Map<String, List<CEASegment>> utterances) {
        this.utterances = utterances;
    }

    public int getSpeakerCount() {
        return utterances.size();
    }

    public double getAudienceActivity() {
        //TODO: implementation
        return audienceActivity;
    }

    public void setAudienceActivity(double audienceActivity) {
        this.audienceActivity = audienceActivity;
    }

    public void addUtterance(Segment segment) {
        String clusterName = segment.getClusterName();

        Duration utteranceStart = Duration.ofSeconds((long) (segment.getStart() / segment.Rate));
        Duration utteranceLenght = Duration.ofSeconds((long) (segment.getLength() / segment.Rate));
        CEASegment ceaSegment = new CEASegment(utteranceStart, utteranceLenght);

        if(!utterances.containsKey(clusterName)) {
            utterances.put(clusterName, new ArrayList<>());
        }

        utterances.get(clusterName).add(ceaSegment);
    }
}
