package video.parser;

import org.junit.Test;
import video.model.CEAChunk;
import video.model.CEAVideo;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class CEAVideoSamplerTest extends BaseParserTest{

    private static final long CHUNK_COUNT = 3;
    private static final int CHUNK_SIZE_SECONDS = 1;

    @Test
    public void sampleChunks() throws Exception {
        CEAVideo video = CEAVideoReader.readFile(exampleVideoFileName());
        CEAVideoSampler sampler = new CEAVideoSampler(video);

        List<CEAChunk> chunks = sampler.sampleChunks(Duration.ofSeconds(CHUNK_SIZE_SECONDS));

        assertEquals(CHUNK_COUNT, chunks.size());
    }

}