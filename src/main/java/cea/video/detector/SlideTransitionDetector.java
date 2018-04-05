package cea.video.detector;

import cea.video.frame_similarity.FrameSimilarityDetector;
import cea.video.frame_similarity.PixelDiffDetector;
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
        FrameSimilarityDetector fsd = new PixelDiffDetector();
        Chunk computedChunk;
        Stack<Chunk> stack = new Stack<>();
        stack.push(chunk);

        while(!stack.empty()) {
            computedChunk = fsd.computeChunkFramesSimiliarity(stack.pop());
            if(stopCondition(computedChunk)) {
                toRet.add(new Detection(computedChunk.getMiddleFrame(), DetectionType.SlideChange, fsd.getClass().getSimpleName()));
                closeChunk(computedChunk);
                continue;
            }

            if(computedChunk.frameMatchesNotComputed()) {
                closeChunk(computedChunk);
                continue;
            }
            checkForSlideTransition(computedChunk, stack, sampler);
        }

        return toRet;
    }

    protected void closeChunk(Chunk chunk) {
        try {
            chunk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean stopCondition(Chunk chunk) {
        long firstChunkFrameTime = chunk.getFirstFrame().getTimestamp().toMillis();
        long lastChunkFrameTime = chunk.getLastFrame().getTimestamp().toMillis();
        return lastChunkFrameTime - firstChunkFrameTime < MILISECONDS_MIN_CHUNK_LEN;
    }

    protected abstract void checkForSlideTransition(Chunk computedChunk, Stack<Chunk> stack, VideoSampler sampler);

}
