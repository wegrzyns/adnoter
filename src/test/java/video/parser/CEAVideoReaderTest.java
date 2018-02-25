package video.parser;

import org.junit.Test;
import org.springframework.util.ResourceUtils;
import video.model.CEAVideo;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class CEAVideoReaderTest {

    private static final String EXAMPLE_VIDEO_FILE_NAME = "/parserTest/testVideo.mp4";
    private static final String EXAMPLE_VIDEO_DIRECTORY_NAME = "/parserTest/testDirectory";
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

    private String exampleVideoFileName() throws FileNotFoundException {
        return resourcePath(EXAMPLE_VIDEO_FILE_NAME);
    }

    private String exampleVideoDirectorName() throws FileNotFoundException {
        return resourcePath(EXAMPLE_VIDEO_DIRECTORY_NAME);
    }

    private String resourcePath(String relativePathToResource) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource(relativePathToResource)).getAbsolutePath();
    }



}