package cea.video.parser;

import cea.video.model.CEASamplerDTO;
import org.junit.Test;
import org.opencv.core.Mat;
import cea.video.model.CEAChunk;
import cea.video.model.CEAFrame;
import cea.video.model.CEAVideo;
import cea.video.parser.stubs.CEAVideoStub;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class CEAVideoSamplerTest extends BaseParserTest{

    private static final long CHUNK_COUNT = 3;
    private static final int CHUNK_SIZE_SECONDS = 1;
    private static final long FIRST_FRAME_EXAMPLE_CHUNK_POSITION = 0;
    private static final long MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION = 60;
    private static final long LAST_FRAME_EXAMPLE_CHUNK_POSITION = 120;
    private static final double FPS = 30;

    @Test
    public void sampleChunks() throws Exception {
        CEAVideo video = CEAVideoReader.readFile(exampleVideoFileName());
        CEAVideoSampler sampler = new CEAVideoSampler(video);
        CEASamplerDTO samplerDTO = new CEASamplerDTO(video, Duration.ofSeconds(CHUNK_SIZE_SECONDS));

        List<CEAChunk> chunks = sampler.sampleChunks(samplerDTO);

        assertEquals(CHUNK_COUNT, chunks.size());
    }

    @Test
    public void leftChunk() throws Exception {
        CEAChunk parentChunk = exampleChunk();
        CEAVideoSampler sampler = exampleSampler();

        CEAChunk leftChunk = sampler.leftChunk(parentChunk);
        assertNotNull(leftChunk);

        long middleFramePosition = MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION/2 + FIRST_FRAME_EXAMPLE_CHUNK_POSITION;

        assertEquals(FIRST_FRAME_EXAMPLE_CHUNK_POSITION, leftChunk.getFirstFrame().getPosition());
        assertEquals(middleFramePosition, leftChunk.getMiddleFrame().getPosition());
        assertEquals(MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION, leftChunk.getLastFrame().getPosition());

        assertEquals( (long) (FIRST_FRAME_EXAMPLE_CHUNK_POSITION/FPS), leftChunk.getFirstFrame().getTimestamp().getSeconds());
        assertEquals( (long) (middleFramePosition/FPS), leftChunk.getMiddleFrame().getTimestamp().getSeconds());
        assertEquals( (long) (MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION/FPS), leftChunk.getLastFrame().getTimestamp().getSeconds());
    }

    @Test
    public void rightChunk() throws Exception {
        CEAChunk parentChunk = exampleChunk();
        CEAVideoSampler sampler = exampleSampler();

        CEAChunk rightChunk = sampler.rightChunk(parentChunk);
        assertNotNull(rightChunk);

        long middleFramePosition = MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION + (LAST_FRAME_EXAMPLE_CHUNK_POSITION - MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION)/2;

        assertEquals(MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION, rightChunk.getFirstFrame().getPosition());
        assertEquals(middleFramePosition, rightChunk.getMiddleFrame().getPosition());
        assertEquals(LAST_FRAME_EXAMPLE_CHUNK_POSITION, rightChunk.getLastFrame().getPosition());

        assertEquals( (long) (MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION/FPS), rightChunk.getFirstFrame().getTimestamp().getSeconds());
        assertEquals( (long) (middleFramePosition/FPS), rightChunk.getMiddleFrame().getTimestamp().getSeconds());
        assertEquals( (long) (LAST_FRAME_EXAMPLE_CHUNK_POSITION/FPS), rightChunk.getLastFrame().getTimestamp().getSeconds());
    }

    private CEAChunk exampleChunk() {
        CEAVideo video = new CEAVideo();
        CEAFrame firstFrame = new CEAFrame(
                new Mat(),
                FIRST_FRAME_EXAMPLE_CHUNK_POSITION,
                Duration.ofSeconds((long) (FIRST_FRAME_EXAMPLE_CHUNK_POSITION/FPS)), video);
        CEAFrame middleFrame = new CEAFrame(
                new Mat(),
                MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION,
                Duration.ofSeconds((long) (MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION/FPS)), video);
        CEAFrame lastFrame = new CEAFrame(
                new Mat(),
                LAST_FRAME_EXAMPLE_CHUNK_POSITION,
                Duration.ofSeconds((long) (LAST_FRAME_EXAMPLE_CHUNK_POSITION/FPS)), video);
        return new CEAChunk(firstFrame, middleFrame, lastFrame);
    }

    private CEAVideoSampler exampleSampler() {
        CEAVideo video = new CEAVideoStub();
        video.setFrameRate(FPS);
        return new CEAVideoSampler(video);
    }

}