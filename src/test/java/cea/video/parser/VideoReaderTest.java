package cea.video.parser;

import org.junit.Test;
import cea.video.model.Video;

import java.util.List;

import static org.junit.Assert.*;

public class VideoReaderTest extends BaseParserTest{

    private static final int EXAMPLE_VIDEO_DIRECTORY_FILE_NUMBER = 3;

    @Test
    public void readFile() throws Exception {
        Video Video = VideoReader.readFile(exampleVideoFileName());

        assertNotNull(Video);
    }

    @Test
    public void readDirectory() throws Exception {
        List<Video> Videos = VideoReader.readDirectory(exampleVideoDirectorName());

        assertNotNull(Videos);
        assertEquals(EXAMPLE_VIDEO_DIRECTORY_FILE_NUMBER, Videos.size());
    }
}