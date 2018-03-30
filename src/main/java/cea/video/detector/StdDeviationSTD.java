package cea.video.detector;

import cea.video.model.CEAChunk;
import cea.video.parser.CEAVideoSampler;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.Stack;

//based on Jeong HJ, Kim T-E, Kim HG, Kim MH (2015) Automatic detection of slide transitions in lecture videos. Multimed Tools Appl 74(18):7537–7554.
public class StdDeviationSTD extends SlideTransitionDetector {

    @Override
    protected void checkForSlideTransition(CEAChunk computedChunk, Stack<CEAChunk> stack, CEAVideoSampler sampler) {
        if(slideTransitionLeftChunk(computedChunk)) {
            stack.push(sampler.leftChunk(computedChunk));
        }

        if(slideTransitionRightChunk(computedChunk)) {
            stack.push(sampler.rightChunk(computedChunk));
        }
    }

    private boolean slideTransitionLeftChunk(CEAChunk computedChunk) {
        int frameSimilarity = computedChunk.getFrameMatch(CEAChunk.FRAME_0_1_MATCH_INDEX);
        return slideTransition(computedChunk, frameSimilarity);
    }

    private boolean slideTransitionRightChunk(CEAChunk computedChunk) {
        int frameSimilarity = computedChunk.getFrameMatch(CEAChunk.FRAME_1_2_MATCH_INDEX);
        return slideTransition(computedChunk, frameSimilarity);
    }

    private boolean slideTransition(CEAChunk chunk, int frameSimilarity) {
        return frameSimilarity < threshold(chunk);
    }


    private double threshold(CEAChunk chunk) {
        StandardDeviation std = new StandardDeviation();
        double frameSimilaritySTD = std.evaluate(chunk.getFrameMatches().stream().mapToDouble(match -> match).toArray());

        return mu(chunk) * (1 - 1 / frameSimilaritySTD);
    }

    private double mu(CEAChunk chunk) {
        int m = chunk.getFrameMatches().size();
        int similaritySum = chunk.getFrameMatches().stream()
                .mapToInt(match -> match)
                .sum();

        return  1.0 / m * similaritySum;
    }
}
