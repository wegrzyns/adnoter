package cea.evaluation.measure;

public class DiarizationMatch {

    private String baselineSpeaker;
    private String hypothesisSpeaker;
    private double diarizationErrorRate;
    private double confusionErrorRate;
    private double missedDetectionsErrorRate;
    private double falseAlarmsErrorRate;

    public DiarizationMatch() {
    }

    public DiarizationMatch(String baselineSpeaker, String hypothesisSpeaker, double diarizationErrorRate) {
        this.baselineSpeaker = baselineSpeaker;
        this.hypothesisSpeaker = hypothesisSpeaker;
        this.diarizationErrorRate = diarizationErrorRate;
    }

    public DiarizationMatch(String baselineSpeaker, String hypothesisSpeaker, double diarizationErrorRate, double confusionErrorRate, double missedDetectionsErrorRate, double falseAlarmsErrorRate) {
        this.baselineSpeaker = baselineSpeaker;
        this.hypothesisSpeaker = hypothesisSpeaker;
        this.diarizationErrorRate = diarizationErrorRate;
        this.confusionErrorRate = confusionErrorRate;
        this.missedDetectionsErrorRate = missedDetectionsErrorRate;
        this.falseAlarmsErrorRate = falseAlarmsErrorRate;
    }

    public String getBaselineSpeaker() {
        return baselineSpeaker;
    }

    public void setBaselineSpeaker(String baselineSpeaker) {
        this.baselineSpeaker = baselineSpeaker;
    }

    public String getHypothesisSpeaker() {
        return hypothesisSpeaker;
    }

    public void setHypothesisSpeaker(String hypothesisSpeaker) {
        this.hypothesisSpeaker = hypothesisSpeaker;
    }

    public double getDiarizationErrorRate() {
        return diarizationErrorRate;
    }

    public void setDiarizationErrorRate(double diarizationErrorRate) {
        this.diarizationErrorRate = diarizationErrorRate;
    }

    public double getConfusionErrorRate() {
        return confusionErrorRate;
    }

    public void setConfusionErrorRate(double confusionErrorRate) {
        this.confusionErrorRate = confusionErrorRate;
    }

    public double getMissedDetectionsErrorRate() {
        return missedDetectionsErrorRate;
    }

    public void setMissedDetectionsErrorRate(double missedDetectionsErrorRate) {
        this.missedDetectionsErrorRate = missedDetectionsErrorRate;
    }

    public double getFalseAlarmsErrorRate() {
        return falseAlarmsErrorRate;
    }

    public void setFalseAlarmsErrorRate(double falseAlarmsErrorRate) {
        this.falseAlarmsErrorRate = falseAlarmsErrorRate;
    }
}
