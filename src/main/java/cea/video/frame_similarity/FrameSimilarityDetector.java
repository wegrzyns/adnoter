package cea.video.frame_similarity;

import cea.video.model.CEAChunk;

public interface FrameSimilarityDetector {

    CEAChunk computeChunkFramesSimiliarity(CEAChunk chunk);
}
