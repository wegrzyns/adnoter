package video.detector;

import video.model.CEAChunk;
import video.model.CEADetection;

import java.util.List;

public interface SlideTransistionDetector {

    List<CEADetection> detect(CEAChunk chunk);
}
