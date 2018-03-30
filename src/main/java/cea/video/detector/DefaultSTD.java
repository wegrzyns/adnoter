package cea.video.detector;

import cea.Util.NumberUtil;
import cea.video.model.CEAChunk;
import cea.video.parser.CEAVideoSampler;

import java.util.List;
import java.util.Stack;

public class DefaultSTD extends SlideTransitionDetector {

    @Override
    protected void checkForSlideTransition(CEAChunk computedChunk, Stack<CEAChunk> stack, CEAVideoSampler sampler) {
        if(slideTransitionLeftChunk(computedChunk)) {
            stack.push(sampler.leftChunk(computedChunk));
        }

        if(slideTransitionRightChunk(computedChunk)) {
            stack.push(sampler.rightChunk(computedChunk));
        }
    }

    private boolean slideTransitionLeftChunk(CEAChunk chunk) {
        double m1 = chunk.getFrameMatch(CEAChunk.FRAME_0_1_MATCH_INDEX);
        double m2 = chunk.getFrameMatch(CEAChunk.FRAME_1_2_MATCH_INDEX);
        double m3 = chunk.getFrameMatch(CEAChunk.FRAME_0_2_MATCH_INDEX);
        double threshold = threshold(chunk);

        if(m1 > threshold) {
            return false;
        }

        if(m2 > threshold && m3 < threshold) {
            return true;
        }

        if(m2 < threshold && m3 > threshold) {
            return true;
        }

        return m2 < threshold && m3 < threshold;

    }

    private boolean slideTransitionRightChunk(CEAChunk chunk) {
        double m1 = chunk.getFrameMatch(CEAChunk.FRAME_0_1_MATCH_INDEX);
        double m2 = chunk.getFrameMatch(CEAChunk.FRAME_1_2_MATCH_INDEX);
        double m3 = chunk.getFrameMatch(CEAChunk.FRAME_0_2_MATCH_INDEX);
        double threshold = threshold(chunk);

        if(m1 > threshold && m2 < threshold && m3 < threshold) {
            return true;
        }

        if(m1 < threshold && m2 < threshold && m3 > threshold) {
            return true;
        }

        return m1 < threshold && m2 < threshold && m3 < threshold;

    }


    private double threshold(CEAChunk chunk) {
        List<Integer> frameMatches = chunk.getFrameMatches();

        double meanAbsoluteDeviation = NumberUtil.meanAbsoluteDevation(frameMatches);
        double mean = NumberUtil.average(frameMatches);

//        if( meanAbsoluteDeviation < mean * 0.15) return -1;

        return mean*(1 - 1/meanAbsoluteDeviation);
    }
}
