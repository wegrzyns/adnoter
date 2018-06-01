package cea.audio;

import cea.App;
import cea.audio.model.DiarizationResult;
import cea.audio.model.DiarizationResultDTO;
import cea.audio.parser.Audio;
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

public class CEADiarization {

    private DiarizationResult diarizationResult;

    private static Logger logger = LoggerFactory.getLogger(CEADiarization.class);

    public CEADiarization() {
        diarizationResult = new DiarizationResult();
    }

    public void launchDiarization(CEABaseline baseline) throws InvocationTargetException, IllegalAccessException {
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

                if (parameter.getParameterDiarization().getSystem() == ParameterBNDiarization.SystemString[1]) {
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

        diarizationResultDTO.getClusterSet().getClusterMap().entrySet()
                .forEach(entry -> entry.getValue().iterator().forEachRemaining(diarizationResult::addUtterance));

    }

    public static void logResults(CEABaseline baseline, Duration executionTime) {
        logger.info("============= Speaker Diarization Algorithm =============");
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withZone( ZoneId.systemDefault() );
        logger.info(String.format("Date: %s", formatter.format(Instant.now())));
        logger.info(String.format("File name: %s", baseline.getFilePath()));
        logger.info(String.format("Overall execution time %s\n", executionTime));
    }
}
