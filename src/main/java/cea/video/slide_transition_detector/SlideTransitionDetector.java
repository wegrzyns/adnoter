package cea.video.slide_transition_detector;

import cea.Util.ConfigurationUtil;
import cea.video.frame_similarity.FrameSimilarityDetector;
import cea.video.frame_similarity.feature.FeatureType;
import cea.video.model.Chunk;
import cea.video.model.Detection;
import cea.video.model.DetectionType;
import cea.video.model.Frame;
import cea.video.parser.VideoSampler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class SlideTransitionDetector {

    private static final long MILISECONDS_MIN_CHUNK_LEN = ConfigurationUtil.configuration().getInt("stopCondition.millisecondMinimalChunkLength");
    private static final FeatureType FEATURE_TYPE = FeatureType.valueOf(ConfigurationUtil.configuration().getString("feature.frameSimilarityDetectionFeatureType"));

    public List<Detection> detect(Chunk chunk, VideoSampler sampler) {
        List<Detection> toRet = new ArrayList<>();
        FrameSimilarityDetector fsd = new FrameSimilarityDetector(FEATURE_TYPE);
        Chunk computedChunk;
        Stack<Chunk> stack = new Stack<>();
        stack.push(chunk);

        while(!stack.empty()) {
            computedChunk = fsd.computeChunkFramesSimiliarity(stack.pop());
            if(stopCondition(computedChunk)) {
                Detection detection = new Detection(computedChunk.getMiddleFrame());

                if(computedChunk.frameMatchesNotComputed()) {
                    detection.setType(DetectionType.SlideRegionToggle);
                }
                else {
                    detection.setType(DetectionType.SlideChange);
                }
                toRet.add(detection);

                closeChunk(computedChunk);
                continue;
            }

            if(computedChunk.frameMatchesNotComputed()) {
                if(!slideRegionChange(computedChunk, stack, sampler)) {
                    closeChunk(computedChunk);
                }
                continue;
            }
            checkForSlideTransition(computedChunk, stack, sampler);
        }

        return toRet;
    }

    private boolean slideRegionChange(Chunk computedChunk, Stack<Chunk> stack, VideoSampler sampler) {
        if(slideRegionToggled(computedChunk)) {
            if(slideRegionToggledLeftChunk(computedChunk)) {
                stack.push(sampler.leftChunk(computedChunk));
            }

            if(slideRegionToggledRightChunk(computedChunk)) {
                stack.push(sampler.rightChunk(computedChunk));
            }

            return true;
        }
        return false;
    }

    private boolean slideRegionToggledLeftChunk(Chunk computedChunk) {
        return computedChunk.getFirstFrame().isSlideRegionDetected()
                != computedChunk.getMiddleFrame().isSlideRegionDetected();
    }

    private boolean slideRegionToggledRightChunk(Chunk computedChunk) {
        return computedChunk.getMiddleFrame().isSlideRegionDetected()
                != computedChunk.getLastFrame().isSlideRegionDetected();
    }

    private boolean slideRegionToggled(Chunk chunk) {
        return chunk.framesAsList().stream().anyMatch(Frame::isSlideRegionDetected) &&
                chunk.framesAsList().stream().anyMatch(frame -> !frame.isSlideRegionDetected());
    }

    void closeChunk(Chunk chunk) {
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
