package cea.video.parser;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

public class BaseParserTest {

    protected static final String EXAMPLE_VIDEO_FILE_NAME = "/parserTest/testVideo.mp4";
    protected static final String EXAMPLE_VIDEO_DIRECTORY_NAME = "/parserTest/testDirectory";

    protected String exampleVideoFileName() throws FileNotFoundException {
        return resourcePath(EXAMPLE_VIDEO_FILE_NAME);
    }

    protected String exampleVideoDirectorName() throws FileNotFoundException {
        return resourcePath(EXAMPLE_VIDEO_DIRECTORY_NAME);
    }

    protected String resourcePath(String relativePathToResource) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource(relativePathToResource)).getAbsolutePath();
    }
}
