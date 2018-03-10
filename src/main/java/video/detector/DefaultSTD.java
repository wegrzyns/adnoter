package video.detector;

import Util.NumberUtil;
import video.model.CEAChunk;
import video.model.CEADetection;
import video.model.CEADetectionType;
import video.parser.CEAVideoSampler;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DefaultSTD implements SlideTransistionDetector {

    private static final long MILISECONDS_MIN_CHUNK_LEN = 1000;

    @Override
    public List<CEADetection> detect(CEAChunk chunk, CEAVideoSampler sampler) {
        List<CEADetection> toRet = new ArrayList<>();
        FrameSimilarityDetector fsd = new SIFTDetector();
        CEAChunk computedChunk;
        Stack<CEAChunk> stack = new Stack<>();
        stack.push(chunk);

        while(!stack.empty()) {
            computedChunk = fsd.computeChunkFramesSimiliarity(stack.pop());
            if(stopCondition(computedChunk)) {
                toRet.add(new CEADetection(computedChunk.getMiddleFrame(), CEADetectionType.SlideChange));
                continue;
            }

            if(computedChunk.frameMatchesNotComputed()) {
                continue;
            }

            if(slideTransitionLeftChunk(computedChunk)) {
                stack.push(sampler.leftChunk(computedChunk));
            }

            if(slideTransitionRightChunk(computedChunk)) {
                stack.push(sampler.rightChunk(computedChunk));
            }
        }

        return toRet;
    }

    private boolean stopCondition(CEAChunk chunk) {
        long firstChunkFrameTime = chunk.getFirstFrame().getTimestamp().toMillis();
        long lastChunkFrameTime = chunk.getLastFrame().getTimestamp().toMillis();
        return lastChunkFrameTime - firstChunkFrameTime < MILISECONDS_MIN_CHUNK_LEN;
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

        return mean*(1 - 1/meanAbsoluteDeviation);
    }
}
