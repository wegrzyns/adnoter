package video;

import Util.JsonUtil;
import evaluation.measure.Measure;
import evaluation.model.CEABaseline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import video.detector.DefaultSTD;
import video.detector.SlideTransistionDetector;
import video.model.CEAChunk;
import video.model.CEADetection;
import video.model.CEAVideo;
import video.parser.CEAVideoReader;
import video.parser.CEAVideoSampler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CEASlideTransitionDetectorManager {

    private static Logger logger = LoggerFactory.getLogger(CEASlideTransitionDetectorManager.class);

    public static List<CEADetection> processVideo(String path) {
        CEAVideo video = CEAVideoReader.readFile(path);
        CEAVideoSampler sampler = new CEAVideoSampler(video);

        SlideTransistionDetector std = new DefaultSTD();
        //TODO: Chunk duration/length to configuration

        List<CEAChunk> videoChunks = sampler.sampleChunks(Duration.ofSeconds(30));

        return videoChunks.parallelStream()
                .map(chunk -> std.detect(chunk, sampler))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static void evaluateAlgorithm(String pathToJsonInput) throws IOException {
        CEABaseline baseline = JsonUtil.evaluationFromJson(pathToJsonInput);
        Instant start = Instant.now();

        List<CEADetection> detections = processVideo(baseline.getFilePath());

        Instant end = Instant.now();
        logger.info(String.format("Overall execution time %s\n", Duration.between(start, end)));

        Measure measure = new Measure(detections, baseline.getSlideTransitions());

        logger.info(String.format("Precision: %f (%f%%)", measure.precision(),  measure.precision()*100));
        logger.info(String.format("Recall: %f (%f%%)", measure.recall(), measure.recall()*100));
        logger.info(String.format("F-measure: %f (%f%%)\n", measure.fMeasure(), measure.fMeasure()*100));

        detections.stream()
                .sorted()
                .forEach(detection -> logger.info(detection.toString()));

    }
}
