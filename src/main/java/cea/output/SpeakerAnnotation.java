package cea.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"speakerCount", "audienceActivity", "utteranceAnnotations"})
public class SpeakerAnnotation {

    private static DecimalFormat df2 = new DecimalFormat(".##");

    private int speakerCount = 0;
    @JsonProperty("audienceActivity")
    private String audienceActivity;
    private List<UtteranceAnnotation> utteranceAnnotations;

    public SpeakerAnnotation() {
    }

    public SpeakerAnnotation(List<UtteranceAnnotation> utteranceAnnotations, int speakerCount) {
        this.utteranceAnnotations = utteranceAnnotations;
        this.speakerCount = speakerCount;
        this.audienceActivity = df2.format(calculateAudienceActivity()) + " %";
    }

    private int calculateSpeakerCount() {
        return utteranceAnnotations.size();
    }

    private double calculateAudienceActivity() {
        Map<String, Double> speakerUtterancesLength = new HashMap<>();
        utteranceAnnotations.forEach(utteranceAnnotation -> {
            addSpeakerUtteranceLenght(speakerUtterancesLength, utteranceAnnotation);
        });

        Double longestSpeakerUtterancesLength = speakerUtterancesLength.values().stream().max(Comparator.comparing(Double::valueOf)).get();
        Double allSpeakersUtterancesLength = speakerUtterancesLength.values().stream().reduce(0.0, (a, b) -> a+b);

        Double audienceUtterancesLength = allSpeakersUtterancesLength - longestSpeakerUtterancesLength;

        return (audienceUtterancesLength / allSpeakersUtterancesLength) * 100;
    }

    private void addSpeakerUtteranceLenght(Map<String, Double> map, UtteranceAnnotation utteranceAnnotation) {
        String speakerName  = utteranceAnnotation.getSpeakerName();
        if(!map.containsKey(speakerName)) {
            map.put(speakerName, (double) utteranceAnnotation.getUtteranceLength().getSeconds());
        }
        else {
            map.put(speakerName, map.get(utteranceAnnotation.getSpeakerName()) + utteranceAnnotation.getUtteranceLength().getSeconds());
        }
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    public void setSpeakerCount(int speakerCount) {
        this.speakerCount = speakerCount;
    }

    public String getAudienceActivity() {
        return audienceActivity;
    }

    public void setAudienceActivity(String audienceActivity) {
        this.audienceActivity = audienceActivity;
    }

    public List<UtteranceAnnotation> getUtteranceAnnotations() {
        return utteranceAnnotations;
    }

    public void setUtteranceAnnotations(List<UtteranceAnnotation> utteranceAnnotations) {
        this.utteranceAnnotations = utteranceAnnotations;
    }
}
