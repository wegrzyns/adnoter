package cea.video.parser;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cea.video.model.Video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class VideoReader {

    private static Logger logger = LoggerFactory.getLogger(VideoReader.class);

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Video readFile(String path) {
        VideoCapture video = new VideoCapture(path);

        if(!video.isOpened()) {
            logger.error(String.format("Path %s does not point to a valid cea.video file", path));
            throw new IllegalArgumentException("Supplied path does not point to a valid cea.video file");
        }

        return createADNVideo(video);
    }

    public static List<Video> readDirectory(String path) throws IOException {

        if(!Files.isDirectory(Paths.get(path))) {
            logger.error(String.format("Path %s does not point to a directory", path));
            throw new IllegalArgumentException("Supplied path does not point to a valid directory");
        }

        List<Video> videos = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(Paths.get(path)) ) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> videos.add(readFile(filePath.toString())));
        }

        return videos;
    }

    private static Video createADNVideo(VideoCapture video) {
        return new Video(video, frameArea(video), frameRate(video), frameCount(video));
    }

    private static double frameArea(VideoCapture video) {
        return video.get(Videoio.CV_CAP_PROP_FRAME_WIDTH) * video.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT);
    }

    private static double frameRate(VideoCapture video) {
        return video.get(Videoio.CV_CAP_PROP_FPS);
    }

    private static long frameCount(VideoCapture video) {
        return (long) video.get(Videoio.CV_CAP_PROP_FRAME_COUNT);
    }
}
