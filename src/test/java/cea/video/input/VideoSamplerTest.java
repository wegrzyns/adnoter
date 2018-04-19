package cea.video.input;

import cea.video.model.Chunk;
import cea.video.model.SamplerDTO;
import org.junit.Test;
import org.opencv.core.Mat;
import cea.video.model.Frame;
import cea.video.model.Video;
import cea.video.input.stubs.VideoStub;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class VideoSamplerTest extends BaseParserTest{

    private static final long CHUNK_COUNT = 3;
    private static final int CHUNK_SIZE_SECONDS = 1;
    private static final long FIRST_FRAME_EXAMPLE_CHUNK_POSITION = 0;
    private static final long MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION = 60;
    private static final long LAST_FRAME_EXAMPLE_CHUNK_POSITION = 120;
    private static final double FPS = 30;

    @Test
    public void sampleChunks() throws Exception {
        Video video = VideoReader.readFile(exampleVideoFileName());
        VideoSampler sampler = new VideoSampler(video);
        SamplerDTO samplerDTO = new SamplerDTO(video, Duration.ofSeconds(CHUNK_SIZE_SECONDS));

        List<Chunk> chunks = sampler.sampleChunks(samplerDTO);

        assertEquals(CHUNK_COUNT, chunks.size());
    }

    @Test
    public void leftChunk() throws Exception {
        Chunk parentChunk = exampleChunk();
        VideoSampler sampler = exampleSampler();

        Chunk leftChunk = sampler.leftChunk(parentChunk);
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
        Chunk parentChunk = exampleChunk();
        VideoSampler sampler = exampleSampler();

        Chunk rightChunk = sampler.rightChunk(parentChunk);
        assertNotNull(rightChunk);

        long middleFramePosition = MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION + (LAST_FRAME_EXAMPLE_CHUNK_POSITION - MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION)/2;

        assertEquals(MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION, rightChunk.getFirstFrame().getPosition());
        assertEquals(middleFramePosition, rightChunk.getMiddleFrame().getPosition());
        assertEquals(LAST_FRAME_EXAMPLE_CHUNK_POSITION, rightChunk.getLastFrame().getPosition());

        assertEquals( (long) (MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION/FPS), rightChunk.getFirstFrame().getTimestamp().getSeconds());
        assertEquals( (long) (middleFramePosition/FPS), rightChunk.getMiddleFrame().getTimestamp().getSeconds());
        assertEquals( (long) (LAST_FRAME_EXAMPLE_CHUNK_POSITION/FPS), rightChunk.getLastFrame().getTimestamp().getSeconds());
    }

    private Chunk exampleChunk() {
        Video video = new Video();
        Frame firstFrame = new Frame(
                new Mat(),
                FIRST_FRAME_EXAMPLE_CHUNK_POSITION,
                Duration.ofSeconds((long) (FIRST_FRAME_EXAMPLE_CHUNK_POSITION/FPS)), video);
        Frame middleFrame = new Frame(
                new Mat(),
                MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION,
                Duration.ofSeconds((long) (MIDDLE_FRAME_EXAMPLE_CHUNK_POSITION/FPS)), video);
        Frame lastFrame = new Frame(
                new Mat(),
                LAST_FRAME_EXAMPLE_CHUNK_POSITION,
                Duration.ofSeconds((long) (LAST_FRAME_EXAMPLE_CHUNK_POSITION/FPS)), video);
        return new Chunk(firstFrame, middleFrame, lastFrame);
    }

    private VideoSampler exampleSampler() {
        Video video = new VideoStub();
        video.setFrameRate(FPS);
        return new VideoSampler(video);
    }

}