package cea.video.detector;

import cea.video.model.Chunk;
import cea.video.parser.VideoSampler;

import java.util.Stack;

public abstract class DefaultSlideTransitionDetector extends SlideTransitionDetector {

    @Override
    protected void checkForSlideTransition(Chunk computedChunk, Stack<Chunk> stack, VideoSampler sampler) {
        boolean discardChunk = true;
        if(slideTransitionLeftChunk(computedChunk)) {
            stack.push(sampler.leftChunk(computedChunk));
            discardChunk = false;
        }

        if(slideTransitionRightChunk(computedChunk)) {
            stack.push(sampler.rightChunk(computedChunk));
            discardChunk = false;
        }

        if(discardChunk) {
            closeChunk(computedChunk);
        }
    }

    protected abstract boolean slideTransitionRightChunk(Chunk computedChunk);

    protected abstract boolean slideTransitionLeftChunk(Chunk computedChunk);
}
