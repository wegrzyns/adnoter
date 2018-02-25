package video.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import video.model.CEAChunk;
import video.model.CEAFrame;
import video.model.CEAVideo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CEAVideoSampler {

    private static Logger logger = LoggerFactory.getLogger(CEAVideoReader.class);

    private CEAVideo video;

    public CEAVideoSampler(CEAVideo video) {
        this.video = video;
    }

    public List<CEAChunk> sampleChunks(Duration duration) {
        long sampledFramesOffset = 0;
        long seconds = duration.getSeconds();
        List<CEAChunk> chunks = new ArrayList<>();
        CEAFrame firstFrame, middleFrame, lastFrame = null;

        while(nextFullChunkAvailable(seconds, sampledFramesOffset)) {
            firstFrame = firstChunkFrame(chunks.size(), lastFrame);
            middleFrame = middleChunkFrame(seconds, sampledFramesOffset);
            lastFrame = lastChunkFrame(seconds, sampledFramesOffset);

            chunks.add(new CEAChunk(firstFrame, middleFrame, lastFrame));
            sampledFramesOffset = lastFrame.getPosition();
        }
        //TODO: frame when less than specified lenght available, dynamic length last chunk
//        video.getVideo().
//        0 or lastChunkFrame ,frameRate*seconds
        return chunks;
    }

    private boolean nextFullChunkAvailable(long seconds, long sampledOffset) {
        return Math.ceil(framesPerSeconds(seconds)) + sampledOffset < video.getFrameCount();
    }

    private CEAFrame firstChunkFrame(long prevChunkNumber, CEAFrame lastFramePrevChunk) {
        if(Objects.equals(prevChunkNumber, 0)) {
           return video.getFrame(0);
        }

        if(lastFramePrevChunk ==  null) {
            logger.error("Last frame can not be null when there are any sampled chunks already, chunk number: %d", prevChunkNumber);
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

    private long remainingVideoSeconds(long sampledFramesOffset) {
        //TODO: use this in main samplin method
        long remainingFramesCount = video.getFrameCount() - sampledFramesOffset;
        return (long) Math.floor(remainingFramesCount / video.getFrameRate());
    }
}
