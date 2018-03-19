package cea.video.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cea.video.model.CEAChunk;
import cea.video.model.CEAFrame;
import cea.video.model.CEASamplerDTO;
import cea.video.model.CEAVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CEAVideoSampler {

    private static Logger logger = LoggerFactory.getLogger(CEAVideoReader.class);

    private CEAVideo video;

    public CEAVideoSampler(CEAVideo video) {
        this.video = video;
    }

    public List<CEAChunk> sampleChunks(CEASamplerDTO samplerArg) {
        long sampledFramesOffset = samplerArg.startFrameOffset();
        List<CEAChunk> chunks = new ArrayList<>();

        while(nextFullChunkAvailable(samplerArg, sampledFramesOffset)) {
            sampledFramesOffset = createChunk(chunks, samplerArg.getChunkLenghtInSeconds(), sampledFramesOffset);
        }

        long remainingSeconds = remainingVideoSeconds(sampledFramesOffset, samplerArg);
        if(remainingSeconds > 0) {
            //TODO: hacky -1 solution, prolly breaks tests, better fix needed (better accuracy)
            createChunk(chunks, remainingSeconds-1, sampledFramesOffset);
        }

        return chunks;
    }

    public CEAChunk leftChunk(CEAChunk parentChunk) {
        CEAChunk newChunk;
        CEAFrame firstFrame = parentChunk.getFirstFrame();
        CEAFrame middleFrame;
        CEAFrame lastFrame = parentChunk.getMiddleFrame();

        middleFrame = middleChunkFrame(firstFrame, lastFrame);

        newChunk = new CEAChunk(firstFrame, middleFrame, lastFrame);
        newChunk.setFrameMatch(CEAChunk.FRAME_0_2_MATCH_INDEX, parentChunk.getFrameMatch(CEAChunk.FRAME_0_1_MATCH_INDEX));

        return newChunk;
    }

    public CEAChunk rightChunk(CEAChunk parentChunk) {
        CEAChunk newChunk;
        CEAFrame firstFrame = parentChunk.getMiddleFrame();
        CEAFrame middleFrame;
        CEAFrame lastFrame = parentChunk.getLastFrame();

        middleFrame = middleChunkFrame(firstFrame, lastFrame);

        newChunk = new CEAChunk(firstFrame, middleFrame, lastFrame);
        newChunk.setFrameMatch(CEAChunk.FRAME_0_2_MATCH_INDEX, parentChunk.getFrameMatch(CEAChunk.FRAME_1_2_MATCH_INDEX));

        return newChunk;
    }

    private boolean nextFullChunkAvailable(CEASamplerDTO samplerArg, long sampledOffset) {
        return Math.ceil(framesPerSeconds(samplerArg.getChunkLenghtInSeconds())) + sampledOffset < samplerArg.endFrameOffset();
    }

    private long createChunk(List<CEAChunk> chunks, long seconds, long sampledFramesOffset) {
        CEAFrame firstFrame, middleFrame, lastFrame;

        firstFrame = firstChunkFrame(chunks.size(), lastFrameOfLastChunk(chunks), sampledFramesOffset);
        middleFrame = middleChunkFrame(seconds, sampledFramesOffset);
        lastFrame = lastChunkFrame(seconds, sampledFramesOffset);

        chunks.add(new CEAChunk(firstFrame, middleFrame, lastFrame));

        return lastFrame.getPosition();
    }

    private CEAFrame firstChunkFrame(long prevChunkNumber, CEAFrame lastFramePrevChunk, long sampledFramesOffset) {
        if(Objects.equals(prevChunkNumber, 0L)) {
           return video.getFrame(sampledFramesOffset);
        }

        if(lastFramePrevChunk ==  null) {
            logger.error(String.format("Last frame can not be null when there are any sampled chunks already, chunk number: %d", prevChunkNumber));
            throw new IllegalArgumentException("Last frame can not be null when there are any sampled chunks already");

        }
        return video.getFrame(lastFramePrevChunk.getPosition() + 1);
    }

    private CEAFrame middleChunkFrame(long seconds, long sampledFramesOffset) {
        return frameByDoubleRoundedDown(framesPerSeconds(seconds)/2 + sampledFramesOffset);
    }

    private CEAFrame lastChunkFrame(long seconds, long sampledFramesOffset) {
        return frameByDoubleRoundedDown(framesPerSeconds(seconds) + sampledFramesOffset);
    }

    private double framesPerSeconds(long seconds) {
        return video.getFrameRate()*seconds;
    }

    private CEAFrame frameByDoubleRoundedDown(double framePosition) {
        return video.getFrame( (long) Math.floor(framePosition));
    }

    private long remainingVideoSeconds(long sampledFramesOffset, CEASamplerDTO samplerArg) {
        long remainingFramesCount = samplerArg.endFrameOffset() - sampledFramesOffset;
        return (long) Math.floor(remainingFramesCount / video.getFrameRate());
    }

    private CEAFrame lastFrameOfLastChunk(List<CEAChunk> chunks) {
        if(chunks.isEmpty()) {
            return null;
        }
        return chunks.get(chunks.size() - 1).getLastFrame();
    }

    private CEAFrame middleChunkFrame(CEAFrame firstChunkFrame, CEAFrame lastChunkFrame) {
        long childSegmentSecondLength = secondsBetweenFrames(firstChunkFrame, lastChunkFrame);

        return middleChunkFrame(childSegmentSecondLength, firstChunkFrame.getPosition());
    }

    private long secondsBetweenFrames(CEAFrame earlierFrame, CEAFrame laterFrame) {
        return laterFrame.getTimestamp().getSeconds() - earlierFrame.getTimestamp().getSeconds();
    }

}
