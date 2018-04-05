package cea.video.detector;

import cea.video.model.Chunk;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

//based on Jeong HJ, Kim T-E, Kim HG, Kim MH (2015) Automatic detection of slide transitions in lecture videos. Multimed Tools Appl 74(18):7537–7554.
public class StdDeviationSTD extends DefaultSlideTransitionDetector {

    @Override
    protected boolean slideTransitionLeftChunk(Chunk computedChunk) {
        int frameSimilarity = computedChunk.getFrameMatch(Chunk.FRAME_0_1_MATCH_INDEX);
        return slideTransition(computedChunk, frameSimilarity);
    }

    @Override
    protected boolean slideTransitionRightChunk(Chunk computedChunk) {
        int frameSimilarity = computedChunk.getFrameMatch(Chunk.FRAME_1_2_MATCH_INDEX);
        return slideTransition(computedChunk, frameSimilarity);
    }

    private boolean slideTransition(Chunk chunk, int frameSimilarity) {
        return frameSimilarity < threshold(chunk);
    }


    private double threshold(Chunk chunk) {
        StandardDeviation std = new StandardDeviation();
        double frameSimilaritySTD = std.evaluate(chunk.getFrameMatches().stream().mapToDouble(match -> match).toArray());

        return mu(chunk) * (1 - 1 / frameSimilaritySTD);
    }

    private double mu(Chunk chunk) {
        int m = chunk.getFrameMatches().size();
        int similaritySum = chunk.getFrameMatches().stream()
                .mapToInt(match -> match)
                .sum();

        return  1.0 / m * similaritySum;
    }
}
