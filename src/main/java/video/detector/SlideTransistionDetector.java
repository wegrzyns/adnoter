package video.detector;

import video.model.CEAChunk;
import video.model.CEADetection;
import video.parser.CEAVideoSampler;

import java.util.List;

public interface SlideTransistionDetector {

    List<CEADetection> detect(CEAChunk chunk, CEAVideoSampler sampler);
}
