package cea.video.parser;

import cea.video.model.Chunk;
import cea.video.model.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cea.video.model.SamplerDTO;
import cea.video.model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoSampler {

    private static Logger logger = LoggerFactory.getLogger(VideoReader.class);

    private Video video;

    public VideoSampler(Video video) {
        this.video = video;
    }

    public List<Chunk> sampleChunks(SamplerDTO samplerArg) {
        long sampledFramesOffset = samplerArg.startFrameOffset();
        List<Chunk> chunks = new ArrayList<>();

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

    public Chunk leftChunk(Chunk parentChunk) {
        Chunk newChunk;
        Frame firstFrame = parentChunk.getFirstFrame();
        Frame middleFrame;
        Frame lastFrame = parentChunk.getMiddleFrame();

        middleFrame = middleChunkFrame(firstFrame, lastFrame);

        newChunk = new Chunk(firstFrame, middleFrame, lastFrame);
        newChunk.setFrameMatch(Chunk.FRAME_0_2_MATCH_INDEX, parentChunk.getFrameMatch(Chunk.FRAME_0_1_MATCH_INDEX));

        return newChunk;
    }

    public Chunk rightChunk(Chunk parentChunk) {
        Chunk newChunk;
        Frame firstFrame = parentChunk.getMiddleFrame();
        Frame middleFrame;
        Frame lastFrame = parentChunk.getLastFrame();

        middleFrame = middleChunkFrame(firstFrame, lastFrame);

        newChunk = new Chunk(firstFrame, middleFrame, lastFrame);
        newChunk.setFrameMatch(Chunk.FRAME_0_2_MATCH_INDEX, parentChunk.getFrameMatch(Chunk.FRAME_1_2_MATCH_INDEX));

        return newChunk;
    }

    private boolean nextFullChunkAvailable(SamplerDTO samplerArg, long sampledOffset) {
        return Math.ceil(framesPerSeconds(samplerArg.getChunkLenghtInSeconds())) + sampledOffset < samplerArg.endFrameOffset();
    }

    private long createChunk(List<Chunk> chunks, long seconds, long sampledFramesOffset) {
        Frame firstFrame, middleFrame, lastFrame;

        firstFrame = firstChunkFrame(chunks.size(), lastFrameOfLastChunk(chunks), sampledFramesOffset);
        middleFrame = middleChunkFrame(seconds, sampledFramesOffset);
        lastFrame = lastChunkFrame(seconds, sampledFramesOffset);

        chunks.add(new Chunk(firstFrame, middleFrame, lastFrame));

        return lastFrame.getPosition();
    }

    private Frame firstChunkFrame(long prevChunkNumber, Frame lastFramePrevChunk, long sampledFramesOffset) {
        if(Objects.equals(prevChunkNumber, 0L)) {
           return video.getFrame(sampledFramesOffset);
        }

        if(lastFramePrevChunk ==  null) {
            logger.error(String.format("Last frame can not be null when there are any sampled chunks already, chunk number: %d", prevChunkNumber));
            throw new IllegalArgumentException("Last frame can not be null when there are any sampled chunks already");

        }
        return video.getFrame(lastFramePrevChunk.getPosition() + 1);
    }

    private Frame middleChunkFrame(long seconds, long sampledFramesOffset) {
        return frameByDoubleRoundedDown(framesPerSeconds(seconds)/2 + sampledFramesOffset);
    }

    private Frame lastChunkFrame(long seconds, long sampledFramesOffset) {
        return frameByDoubleRoundedDown(framesPerSeconds(seconds) + sampledFramesOffset);
    }

    private double framesPerSeconds(long seconds) {
        return video.getFrameRate()*seconds;
    }

    private Frame frameByDoubleRoundedDown(double framePosition) {
        return video.getFrame( (long) Math.floor(framePosition));
    }

    private long remainingVideoSeconds(long sampledFramesOffset, SamplerDTO samplerArg) {
        long remainingFramesCount = samplerArg.endFrameOffset() - sampledFramesOffset;
        return (long) Math.floor(remainingFramesCount / video.getFrameRate());
    }

    private Frame lastFrameOfLastChunk(List<Chunk> chunks) {
        if(chunks.isEmpty()) {
            return null;
        }
        return chunks.get(chunks.size() - 1).getLastFrame();
    }

    private Frame middleChunkFrame(Frame firstChunkFrame, Frame lastChunkFrame) {
        long childSegmentSecondLength = secondsBetweenFrames(firstChunkFrame, lastChunkFrame);

        return middleChunkFrame(childSegmentSecondLength, firstChunkFrame.getPosition());
    }

    private long secondsBetweenFrames(Frame earlierFrame, Frame laterFrame) {
        return laterFrame.getTimestamp().getSeconds() - earlierFrame.getTimestamp().getSeconds();
    }

}
