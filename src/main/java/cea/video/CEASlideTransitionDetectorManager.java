package cea.video;

import cea.Util.JsonUtil;
import cea.evaluation.measure.Measure;
import cea.evaluation.model.CEABaseline;
import cea.video.detector.DefaultSTD;
import cea.video.detector.StdDeviationSTD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cea.video.detector.SlideTransitionDetector;
import cea.video.model.CEAChunk;
import cea.video.model.CEADetection;
import cea.video.model.CEASamplerDTO;
import cea.video.model.CEAVideo;
import cea.video.parser.CEAVideoReader;
import cea.video.parser.CEAVideoSampler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CEASlideTransitionDetectorManager {

    private static final int CHUNK_DURATION_SECONDS = 10;

    private static Logger logger = LoggerFactory.getLogger(CEASlideTransitionDetectorManager.class);

    public static List<CEADetection> processVideo(String path) {
        CEAVideo video = CEAVideoReader.readFile(path);
        CEAVideoSampler sampler = new CEAVideoSampler(video);

        SlideTransitionDetector std = new StdDeviationSTD();
        //TODO: Chunk duration/length to configuration
        //TODO: Video offset(from-tO) for fragment testing, to configuration

        CEASamplerDTO samplerDTO = new CEASamplerDTO(video, Duration.ofSeconds(CHUNK_DURATION_SECONDS));
//        samplerDTO.setSamplingStart(Duration.ofMinutes(5).plusSeconds(30));
//        samplerDTO.setSamplingEnd(Duration.ofMinutes(5).plusSeconds(31));
        List<CEAChunk> videoChunks = sampler.sampleChunks(samplerDTO);

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

        logger.info("============= Slide Transition Detection Algorithm =============");
        logger.info(String.format("File name: %s",baseline.getFilePath()));
        logger.info(String.format("Overall execution time %s\n", Duration.between(start, end)));

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
