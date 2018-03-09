package video.detector;

import video.model.CEAChunk;

public interface FrameSimilarityDetector {

    CEAChunk computeChunkFramesSimiliarity(CEAChunk chunk);
}
