package cea.video.detector;

import cea.video.model.CEAChunk;
import cea.video.model.CEADetection;
import cea.video.parser.CEAVideoSampler;

import java.util.List;

public interface SlideTransistionDetector {

    List<CEADetection> detect(CEAChunk chunk, CEAVideoSampler sampler);
}
