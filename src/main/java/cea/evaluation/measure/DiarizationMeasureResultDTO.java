package cea.evaluation.measure;

public class DiarizationMeasureResultDTO {

    private double summedDiarizationErrorRate;
    private double summedConfusionErrorRate;
    private double summedMissedDetectionsErrorRate;
    private double summedFalseAlarmsErrorRate;

    public DiarizationMeasureResultDTO(double summedDiarizationErrorRate, double summedConfusionErrorRate, double summedMissedDetectionsErrorRate, double summedFalseAlarmsErrorRate) {
        this.summedDiarizationErrorRate = summedDiarizationErrorRate;
        this.summedConfusionErrorRate = summedConfusionErrorRate;
        this.summedMissedDetectionsErrorRate = summedMissedDetectionsErrorRate;
        this.summedFalseAlarmsErrorRate = summedFalseAlarmsErrorRate;
    }

    public double getSummedDiarizationErrorRate() {
        return summedDiarizationErrorRate;
    }

    public void setSummedDiarizationErrorRate(double summedDiarizationErrorRate) {
        this.summedDiarizationErrorRate = summedDiarizationErrorRate;
    }

    public double getSummedConfusionErrorRate() {
        return summedConfusionErrorRate;
    }

    public void setSummedConfusionErrorRate(double summedConfusionErrorRate) {
        this.summedConfusionErrorRate = summedConfusionErrorRate;
    }

    public double getSummedMissedDetectionsErrorRate() {
        return summedMissedDetectionsErrorRate;
    }

    public void setSummedMissedDetectionsErrorRate(double summedMissedDetectionsErrorRate) {
        this.summedMissedDetectionsErrorRate = summedMissedDetectionsErrorRate;
    }

    public double getSummedFalseAlarmsErrorRate() {
        return summedFalseAlarmsErrorRate;
    }

    public void setSummedFalseAlarmsErrorRate(double summedFalseAlarmsErrorRate) {
        this.summedFalseAlarmsErrorRate = summedFalseAlarmsErrorRate;
    }
}
