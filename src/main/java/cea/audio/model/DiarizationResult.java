package cea.audio.model;

import cea.output.SpeakerAnnotation;
import cea.output.UtteranceAnnotation;
import fr.lium.spkDiarization.libClusteringData.Segment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiarizationResult {

    private Map<String, List<CEASpeakerSegment>> utterances;
    private double audienceActivity;

    public DiarizationResult() {
        utterances = new HashMap<>();
    }

    public DiarizationResult(Map<String, List<CEASpeakerSegment>> utterances, double audienceActivity) {
        this.utterances = utterances;
        this.audienceActivity = audienceActivity;
    }

    public Map<String, List<CEASpeakerSegment>> getUtterances() {
        return utterances;
    }

    public void setUtterances(Map<String, List<CEASpeakerSegment>> utterances) {
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
        CEASpeakerSegment ceaSpeakerSegment = new CEASpeakerSegment(utteranceStart, utteranceLenght);

        if(!utterances.containsKey(clusterName)) {
            utterances.put(clusterName, new ArrayList<>());
        }

        utterances.get(clusterName).add(ceaSpeakerSegment);
    }

    public SpeakerAnnotation getSpeakerAnnotation() {
        List<UtteranceAnnotation> utteranceAnnotations = new ArrayList<>();

        getUtterances().forEach((speakerName, speakerUtterances) -> speakerUtterances.forEach(utterance -> {
            Duration utteranceStart = utterance.getTimestamp();
            Duration utteranceLength = utterance.getLength();
            utteranceAnnotations.add(new UtteranceAnnotation(speakerName, utteranceStart, utteranceLength));
        }));

        return new SpeakerAnnotation(utteranceAnnotations, getSpeakerCount());
    }
}
