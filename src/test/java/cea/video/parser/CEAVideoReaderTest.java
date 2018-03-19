package cea.video.parser;

import org.junit.Test;
import cea.video.model.CEAVideo;

import java.util.List;

import static org.junit.Assert.*;

public class CEAVideoReaderTest extends BaseParserTest{

    private static final int EXAMPLE_VIDEO_DIRECTORY_FILE_NUMBER = 3;

    @Test
    public void readFile() throws Exception {
        CEAVideo CEAVideo = CEAVideoReader.readFile(exampleVideoFileName());

        assertNotNull(CEAVideo);
    }

    @Test
    public void readDirectory() throws Exception {
        List<CEAVideo> CEAVideos = CEAVideoReader.readDirectory(exampleVideoDirectorName());

        assertNotNull(CEAVideos);
        assertEquals(EXAMPLE_VIDEO_DIRECTORY_FILE_NUMBER, CEAVideos.size());
    }
}