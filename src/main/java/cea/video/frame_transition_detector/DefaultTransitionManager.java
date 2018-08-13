package cea.video.frame_transition_detector;

import cea.video.model.Chunk;
import cea.video.input.VideoSampler;

import java.util.Stack;

public abstract class DefaultTransitionManager {


    protected boolean checkForTransition(Chunk computedChunk, Stack<Chunk> stack, VideoSampler sampler) {
        boolean discardChunk = true;
        boolean isTransitionPresent = false;

        if(transitionLeftChunk(computedChunk)) {
            stack.push(sampler.leftChunk(computedChunk));
            isTransitionPresent = true;
        }

        if(transitionRightChunk(computedChunk)) {
            stack.push(sampler.rightChunk(computedChunk));
            isTransitionPresent = true;
        }

        if(!isTransitionPresent) {
            FrameTransitionManager.closeChunk(computedChunk);
        }

        return isTransitionPresent;
    }

    protected abstract boolean transitionRightChunk(Chunk computedChunk);

    protected abstract boolean transitionLeftChunk(Chunk computedChunk);
}
