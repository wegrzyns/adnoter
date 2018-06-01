package cea.audio.model;

import fr.lium.spkDiarization.libClusteringData.ClusterSet;
import fr.lium.spkDiarization.parameter.Parameter;

public class DiarizationResultDTO {

    private Parameter parameter;
    private ClusterSet clusterSet;
    private boolean isLast = false;

    public DiarizationResultDTO(Parameter parameter, ClusterSet clusterSet) {
        this.parameter = parameter;
        this.clusterSet = clusterSet;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public ClusterSet getClusterSet() {
        return clusterSet;
    }

    public void setClusterSet(ClusterSet clusterSet) {
        this.clusterSet = clusterSet;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
