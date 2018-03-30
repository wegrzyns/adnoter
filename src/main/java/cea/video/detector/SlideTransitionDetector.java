package cea.video.detector;

import cea.video.frame_similarity.FrameSimilarityDetector;
import cea.video.frame_similarity.SIFTDetector;
import cea.video.model.CEAChunk;
import cea.video.model.CEADetection;
import cea.video.model.CEADetectionType;
import cea.video.parser.CEAVideoSampler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class SlideTransitionDetector {

    //TODO: stop condition threshold to configuration
    protected static final long MILISECONDS_MIN_CHUNK_LEN = 1000;

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
            checkForSlideTransition(computedChunk, stack, sampler);
        }

        return toRet;
    }

    private boolean stopCondition(CEAChunk chunk) {
        long firstChunkFrameTime = chunk.getFirstFrame().getTimestamp().toMillis();
        long lastChunkFrameTime = chunk.getLastFrame().getTimestamp().toMillis();
        return lastChunkFrameTime - firstChunkFrameTime < MILISECONDS_MIN_CHUNK_LEN;
    }

    protected abstract void checkForSlideTransition(CEAChunk computedChunk, Stack<CEAChunk> stack, CEAVideoSampler sampler);

}
