package cea.video.frame_transition_detector;

import cea.Util.ConfigurationUtil;
import cea.video.frame_similarity.FrameSimilarityDetector;
import cea.video.frame_similarity.feature.FeatureType;
import cea.video.model.*;
import cea.video.input.VideoSampler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FrameTransitionManager {

    private static final long MILISECONDS_MIN_CHUNK_LEN = ConfigurationUtil.configuration().getInt("stopCondition.millisecondMinimalChunkLength");
    private static final FeatureType FEATURE_TYPE = FeatureType.valueOf(ConfigurationUtil.configuration().getString("feature.frameSimilarityDetectionFeatureType"));

    private Video video;
    private VideoSampler sampler;

    public FrameTransitionManager(Video video, VideoSampler sampler) {
        this.video = video;
        this.sampler = sampler;
    }

    public List<Detection> detect(Chunk chunk) {
        List<Detection> toRet = new ArrayList<>();
        FrameSimilarityDetector fsd = new FrameSimilarityDetector(FEATURE_TYPE);
        DefaultTransitionManager slideTransitionDetector = new StdDeviationSTD();
        BlackFramesTransitionDetector blackFramesTransitionDetector = new BlackFramesTransitionDetector(video);
        Chunk computedChunk;
        Stack<Chunk> stack = new Stack<>();
        stack.push(chunk);

        while(!stack.empty()) {
            computedChunk = fsd.computeChunkFramesSimiliarity(stack.pop());
            if(stopCondition(computedChunk)) {
                //TODO: better handling, this is dirty hax
                computedChunk.getLastFrame().setTimestamp(computedChunk.getMiddleFrame().getTimestamp());
                Detection detection = new Detection(computedChunk.getLastFrame());

                if(computedChunk.frameMatchesNotComputed()) {
                    detection.setType(DetectionType.SLIDE_REGION_TOGGLE);
                }
                if(computedChunk.isBlackFramesTransition()) {
                    detection.setType(DetectionType.BLACK_FRAMES_TRANSITION);
                }
                else {
                    detection.setType(DetectionType.SLIDE_CHANGE);
                }
                toRet.add(detection);

                closeChunk(computedChunk);
                continue;
            }

//            if(blackFramesTransitionDetector.checkForTransition(computedChunk, stack, sampler)) {
//               continue;
//            };

            if(computedChunk.frameMatchesNotComputed()) {
                if(!slideRegionChange(computedChunk, stack, sampler)) {
                    closeChunk(computedChunk);
                }
                continue;
            }
            if(!slideTransitionDetector.checkForTransition(computedChunk, stack, sampler)) {
                blackFramesTransitionDetector.checkForTransition(computedChunk, stack, sampler);
            }
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

    public static void closeChunk(Chunk chunk) {
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

}
