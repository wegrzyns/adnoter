package cea.evaluation.measure;

public class DiarizationMatch {

    private String baselineSpeaker;
    private String hypothesisSpeaker;
    private double nonMatchedTime;

    public DiarizationMatch() {
    }

    public DiarizationMatch(String baselineSpeaker, String hypothesisSpeaker, double nonMatchedTime) {
        this.baselineSpeaker = baselineSpeaker;
        this.hypothesisSpeaker = hypothesisSpeaker;
        this.nonMatchedTime = nonMatchedTime;
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

    public double getNonMatchedTime() {
        return nonMatchedTime;
    }

    public void setNonMatchedTime(double nonMatchedTime) {
        this.nonMatchedTime = nonMatchedTime;
    }
}
