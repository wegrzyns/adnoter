package cea.evaluation.measure;

public class DiarizationMatch {

    private String baselineSpeaker;
    private String hypothesisSpeaker;
    private double diarizationErrorRate;

    public DiarizationMatch() {
    }

    public DiarizationMatch(String baselineSpeaker, String hypothesisSpeaker, double diarizationErrorRate) {
        this.baselineSpeaker = baselineSpeaker;
        this.hypothesisSpeaker = hypothesisSpeaker;
        this.diarizationErrorRate = diarizationErrorRate;
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
}
