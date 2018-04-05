package cea.video;

import cea.Util.JsonUtil;
import cea.evaluation.measure.Measure;
import cea.evaluation.model.CEABaseline;
import cea.video.detector.StdDeviationSTD;
import cea.video.model.Detection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cea.video.detector.SlideTransitionDetector;
import cea.video.model.Chunk;
import cea.video.model.SamplerDTO;
import cea.video.model.Video;
import cea.video.parser.VideoReader;
import cea.video.parser.VideoSampler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CEASlideTransitionDetectorManager {

    private static final int CHUNK_DURATION_SECONDS = 10;

    private static Logger logger = LoggerFactory.getLogger(CEASlideTransitionDetectorManager.class);

    public static List<Detection> processVideo(String path) {
        Video video = VideoReader.readFile(path);
        VideoSampler sampler = new VideoSampler(video);

        SlideTransitionDetector std = new StdDeviationSTD();
        //TODO: Chunk duration/length to configuration
        //TODO: Video offset(from-tO) for fragment testing, to configuration

        SamplerDTO samplerDTO = new SamplerDTO(video, Duration.ofSeconds(CHUNK_DURATION_SECONDS));
//        samplerDTO.setSamplingStart(Duration.ofMinutes(5).plusSeconds(30));
//        samplerDTO.setSamplingEnd(Duration.ofMinutes(5).plusSeconds(31));
        List<Chunk> videoChunks = sampler.sampleChunks(samplerDTO);

        return videoChunks.parallelStream()
                .map(chunk -> std.detect(chunk, sampler))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static void evaluateAlgorithm(String pathToJsonInput) throws IOException {
        CEABaseline baseline = JsonUtil.evaluationFromJson(pathToJsonInput);
        Instant start = Instant.now();

        List<Detection> detections = processVideo(baseline.getFilePath());

        Instant end = Instant.now();

        logger.info("============= Slide Transition Detection Algorithm =============");
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withZone( ZoneId.systemDefault() );
        logger.info(String.format("Date: %s", formatter.format(Instant.now())));
        logger.info(String.format("File name: %s",baseline.getFilePath()));
        logger.info(String.format("Overall execution time %s\n", Duration.between(start, end)));

        //TODO: feature detector name should be pulled from configuration, temporary solution
        logger.info(String.format("Feature detection algorithm: %s", detections.get(0).getFeatureDetectorName()));
        logger.info(String.format("Detection Resolution: %.2f s", CHUNK_DURATION_SECONDS/2.0));

        Measure measure = new Measure(detections, baseline.getSlideTransitions());

        logger.info(String.format("Precision: %f (%f%%)", measure.precision(),  measure.precision()*100));
        logger.info(String.format("Recall: %f (%f%%)", measure.recall(), measure.recall()*100));
        logger.info(String.format("F-measure: %f (%f%%)\n", measure.fMeasure(), measure.fMeasure()*100));

        detections.stream()
                .sorted()
                .forEach(detection -> logger.info(detection.toString()));

    }
}
