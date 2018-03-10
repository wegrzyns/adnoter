package video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import video.detector.DefaultSTD;
import video.detector.SlideTransistionDetector;
import video.model.CEAChunk;
import video.model.CEADetection;
import video.model.CEAVideo;
import video.parser.CEAVideoReader;
import video.parser.CEAVideoSampler;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CEASlideTransitionDetectorManager {

    private static Logger logger = LoggerFactory.getLogger(CEASlideTransitionDetectorManager.class);

    public static void processVideo(String path) {
        CEAVideo video = CEAVideoReader.readFile(path);
        CEAVideoSampler sampler = new CEAVideoSampler(video);

        SlideTransistionDetector std = new DefaultSTD();
        //TODO: Chunk duration/length to configuration
        Instant start = Instant.now();
        List<CEAChunk> videoChunks = sampler.sampleChunks(Duration.ofSeconds(30));
        List<CEADetection> detections = videoChunks.parallelStream()
                .map(chunk -> std.detect(chunk, sampler))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Instant end = Instant.now();
        logger.info(String.format("Overall execution time %s\n", Duration.between(start, end)));

        detections.stream()
                .sorted()
                .forEach(detection -> logger.info(detection.toString()));

    }
}
