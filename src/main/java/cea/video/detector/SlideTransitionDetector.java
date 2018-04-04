package cea.video.detector;

import cea.video.frame_similarity.FrameSimilarityDetector;
import cea.video.frame_similarity.SIFTDetector;
import cea.video.model.Chunk;
import cea.video.model.Detection;
import cea.video.model.DetectionType;
import cea.video.parser.VideoSampler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class SlideTransitionDetector {

    //TODO: stop condition threshold to configuration
    protected static final long MILISECONDS_MIN_CHUNK_LEN = 1000;

    public List<Detection> detect(Chunk chunk, VideoSampler sampler) {
        List<Detection> toRet = new ArrayList<>();
        FrameSimilarityDetector fsd = new SIFTDetector();
        Chunk computedChunk;
        Stack<Chunk> stack = new Stack<>();
        stack.push(chunk);

        while(!stack.empty()) {
            computedChunk = fsd.computeChunkFramesSimiliarity(stack.pop());
            if(stopCondition(computedChunk)) {
                toRet.add(new Detection(computedChunk.getMiddleFrame(), DetectionType.SlideChange));
                continue;
            }

            if(computedChunk.frameMatchesNotComputed()) {
                continue;
            }
            checkForSlideTransition(computedChunk, stack, sampler);
        }

        return toRet;
    }

    private boolean stopCondition(Chunk chunk) {
        long firstChunkFrameTime = chunk.getFirstFrame().getTimestamp().toMillis();
        long lastChunkFrameTime = chunk.getLastFrame().getTimestamp().toMillis();
        return lastChunkFrameTime - firstChunkFrameTime < MILISECONDS_MIN_CHUNK_LEN;
    }

    protected abstract void checkForSlideTransition(Chunk computedChunk, Stack<Chunk> stack, VideoSampler sampler);

}
