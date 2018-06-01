package cea.video;

import cea.Util.ConfigurationUtil;
import cea.evaluation.measure.Measure;
import cea.evaluation.measure.SlideTransitionTypeFilter;
import cea.evaluation.model.CEABaseline;
import cea.evaluation.model.SlideTransition;
import cea.video.slide_region.DefaultDetector;
import cea.video.slide_transition_detector.StdDeviationSTD;
import cea.video.model.Detection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cea.video.slide_transition_detector.SlideTransitionDetector;
import cea.video.model.Chunk;
import cea.video.model.SamplerDTO;
import cea.video.model.Video;
import cea.video.input.VideoReader;
import cea.video.input.VideoSampler;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CEASlideTransitionDetectorManager {

    private static final int CHUNK_DURATION_SECONDS = ConfigurationUtil.configuration().getInt("chunkDurationSeconds");
    private static final int SAMPLING_START_MINUTES = ConfigurationUtil.configuration().getInt("sampling.startMinutes", -1);
    private static final int SAMPLING_START_SECONDS = ConfigurationUtil.configuration().getInt("sampling.startSeconds", -1);
    private static final int SAMPLING_END_MINUTES = ConfigurationUtil.configuration().getInt("sampling.endMinutes", -1);
    private static final int SAMPLING_END_SECONDS = ConfigurationUtil.configuration().getInt("sampling.endSeconds", -1);

    private static Logger logger = LoggerFactory.getLogger(CEASlideTransitionDetectorManager.class);

    public static List<Detection> processVideo(String path) {
        Video video = VideoReader.readFile(path);
        VideoSampler sampler = new VideoSampler(video);

        SlideTransitionDetector std = new StdDeviationSTD();

        SamplerDTO samplerDTO = new SamplerDTO(video, Duration.ofSeconds(CHUNK_DURATION_SECONDS));
        samplerDTO = fillSamplingDuration(samplerDTO);
        List<Chunk> videoChunks = sampler.sampleChunks(samplerDTO);

        return videoChunks.parallelStream()
                .map(chunk -> std.detect(chunk, sampler))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static SamplerDTO fillSamplingDuration(SamplerDTO samplerDTO) {
        if(SAMPLING_START_MINUTES > -1 && SAMPLING_END_SECONDS > -1) {
            samplerDTO.setSamplingStart(Duration.ofMinutes(SAMPLING_START_MINUTES).plusSeconds(SAMPLING_START_SECONDS));
        }
        if(SAMPLING_END_MINUTES > -1 && SAMPLING_END_SECONDS > -1) {
            samplerDTO.setSamplingEnd(Duration.ofMinutes(SAMPLING_END_MINUTES).plusSeconds(SAMPLING_END_SECONDS));
        }
        return samplerDTO;
    }

    public static void logSlideDetectionResult(List<Detection> detections, CEABaseline baseline, Duration executionTime) {
        logger.info("============= Slide Transition Detection Algorithm =============");
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withZone( ZoneId.systemDefault() );
        logger.info(String.format("Date: %s", formatter.format(Instant.now())));
        logger.info(String.format("File name: %s", baseline.getFilePath()));
        printSamplingDuration();
        logger.info(String.format("Overall execution time %s\n", executionTime));


        ConfigurationUtil.configuration()
                .getKeys("slideRegion")
                .forEachRemaining(key -> logger.info(formattedParameter(key)));
        logger.info("Slide region detection operations:");
        DefaultDetector.slideRegionOperations()
                .stream()
                .map(string -> "    " + string)
                .forEach(logger::info);

        logger.info(String.format("Feature detection algorithm: %s", ConfigurationUtil.configuration().getString("feature.frameSimilarityDetectionFeatureType")));
        logger.info(String.format("Detection Resolution: %.2f s", CHUNK_DURATION_SECONDS/2.0));
        logger.info(formattedParameter("feature.gaussianBlurKernelSize")+"\n");

        List<Duration> baselineTimestamps = baseline.getSlideTransitions().stream()
                .filter(SlideTransitionTypeFilter::filterSlideTransitions)
                .map(SlideTransition::getTimestamp)
                .collect(Collectors.toList());

        Measure measure = new Measure(detections, baselineTimestamps);

        logger.info(String.format("Precision: %f (%f%%)", measure.precision(),  measure.precision()*100));
        logger.info(String.format("Recall: %f (%f%%)", measure.recall(), measure.recall()*100));
        logger.info(String.format("F-measure: %f (%f%%)\n", measure.fMeasure(), measure.fMeasure()*100));

        detections.stream()
                .sorted()
                .forEach(detection -> logger.info(detection.toString()));
    }

    private static void printSamplingDuration() {
        if(SAMPLING_START_MINUTES > -1 && SAMPLING_END_SECONDS > -1) {
            logger.info(String.format("Sampling start: %s", Duration.ofMinutes(SAMPLING_START_MINUTES).plusSeconds(SAMPLING_START_SECONDS)));
        }
        if(SAMPLING_END_MINUTES > -1 && SAMPLING_END_SECONDS > -1) {
            logger.info(String.format("Sampling end: %s", Duration.ofMinutes(SAMPLING_END_MINUTES).plusSeconds(SAMPLING_END_SECONDS)));
        }
    }

    private static String formattedParameter(String configurationKey) {
        String key = ConfigurationUtil.configuration().getKeys(configurationKey).next();
        String value = ConfigurationUtil.configuration().getString(key);

        return String.format("%s = %s", key, value);
    }
}
