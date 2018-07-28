package cea.audio;

import cea.App;
import cea.audio.model.CEASpeakerSegment;
import cea.audio.model.DiarizationResult;
import cea.audio.model.DiarizationResultDTO;
import cea.audio.parser.Audio;
import cea.evaluation.measure.DiarizationMeasure;
import cea.evaluation.measure.DiarizationMeasureResultDTO;
import cea.evaluation.model.CEABaseline;
import fr.lium.spkDiarization.lib.DiarizationException;
import fr.lium.spkDiarization.lib.SpkDiarizationLogger;
import fr.lium.spkDiarization.parameter.Parameter;
import fr.lium.spkDiarization.parameter.ParameterBNDiarization;
import fr.lium.spkDiarization.system.Diarization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class CEADiarization {

    private DiarizationResult diarizationResult;

    private static Logger logger = LoggerFactory.getLogger(CEADiarization.class);

    public CEADiarization() {
        diarizationResult = new DiarizationResult();
    }

    public void launchDiarization(CEABaseline baseline) throws InvocationTargetException, IllegalAccessException {
        //TODO: after diariziation completes or crashes delete temporary converted .wav file
        String[] args = prepareInput(baseline);
        try {
            SpkDiarizationLogger.setup();

            Parameter parameter = Diarization.getParameter(args);
            if (args.length <= 1) {
                parameter.help = true;
            }

            parameter.logCmdLine(args);
            Diarization.info(parameter, "Diarization");
            if (!parameter.show.isEmpty()) {

                Diarization diarization = new Diarization((obj, arg) -> updateDiarizationResult((DiarizationResultDTO) arg));

                if (Objects.equals(parameter.getParameterDiarization().getSystem(), ParameterBNDiarization.SystemString[1])) {
                    parameter.getParameterSegmentationSplit().setSegmentMaximumLength(10 * parameter.getParameterSegmentationInputFile().getRate());
                }

                logger.info("Diarization tuning");
                diarization.ester2DiarizationCorpus(parameter);
            }
        } catch (DiarizationException var3) {
            logger.error("Diarization error", var3);
            var3.printStackTrace();
        } catch (IOException var4) {
            logger.error("IOExecption error", var4);
            var4.printStackTrace();
        } catch (Exception var5) {
            logger.error("Execption error", var5);
            var5.printStackTrace();
        }
    }

    private String[] prepareInput(CEABaseline baseline) {
        //TODO: string to configuration
        //TODO: temp directory?
        Audio audio = new Audio(baseline.getFilePath());
        try {
            audio.parseAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "--fInputMask=./test.wav --sOutputMask=./showName.seg --doCEClustering  showName".split("\\s");
    }

    private void updateDiarizationResult(DiarizationResultDTO diarizationResultDTO) {

        if(diarizationResultDTO.isLast()) {
            App.diarizationResultCallback(diarizationResult);
        }

        diarizationResultDTO.getClusterSet().getClusterMap()
                .forEach((key, value) -> value.iterator().forEachRemaining(diarizationResult::addUtterance));

    }

    public static void logResults(CEABaseline baseline, Duration executionTime, DiarizationResult diarizationResult) {
        logger.info("============= Speaker Diarization Algorithm =============");
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withZone( ZoneId.systemDefault() );
        logger.info(String.format("Date: %s", formatter.format(Instant.now())));
        logger.info(String.format("File name: %s", baseline.getFilePath()));
        logger.info(String.format("Overall execution time %s\n", executionTime));

        DiarizationMeasure diarizationMeasure = new DiarizationMeasure(diarizationResult, createBaselineSpeakerUtterancemap(baseline));
        DiarizationMeasureResultDTO diarizationEvaluationResult = diarizationMeasure.diarizationEvaluationResult();

        double confusionErrorRate = diarizationEvaluationResult.getSummedConfusionErrorRate();
        double missedDetectionsErrorRate = diarizationEvaluationResult.getSummedMissedDetectionsErrorRate();
        double falseAlarmsErrorRate = diarizationEvaluationResult.getSummedFalseAlarmsErrorRate();
        double diarizationErrorRate = diarizationEvaluationResult.getSummedDiarizationErrorRate();

        logger.info(String.format("Confusion error rate: %f (%f%%)", confusionErrorRate, confusionErrorRate * 100));
        logger.info(String.format("Missed detections error rate: %f (%f%%)", missedDetectionsErrorRate, missedDetectionsErrorRate * 100));
        logger.info(String.format("False alarms error rate: %f (%f%%)", falseAlarmsErrorRate, falseAlarmsErrorRate * 100));
        logger.info(String.format("Diarization error rate: %f (%f%%)", diarizationErrorRate, diarizationErrorRate * 100));

    }

    private static Map<String, List<CEASpeakerSegment>> createBaselineSpeakerUtterancemap(CEABaseline baseline) {
        Map<String, List<CEASpeakerSegment>> baselineSpeakerUtterancesMap = new HashMap<>();

        baseline.getSpeakerUtterances().forEach(speakerUtterance -> {
            String speakerName = speakerUtterance.getSpeaker();
            if(!baselineSpeakerUtterancesMap.containsKey(speakerName)) {
                baselineSpeakerUtterancesMap.put(speakerName, new ArrayList<>());
            }
            baselineSpeakerUtterancesMap.get(speakerName).add(new CEASpeakerSegment(speakerUtterance.getTimestamp(), speakerUtterance.getLength()));
        });

        return baselineSpeakerUtterancesMap;
    }
}
