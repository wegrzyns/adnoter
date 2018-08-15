package cea.audio.parser;

import cea.Util.ConsoleOutputCapturer;
import cea.audio.model.SilenceDetection;
import cea.audio.model.SilenceDetectionResult;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class FFmpegAnnotationOutput {

    public static SilenceDetectionResult detectSilence(String filePath) throws IOException {
        FFmpegBuilder ffMpegBuilder = new FFmpegBuilder();

        ffMpegBuilder
                .setInput(filePath)
                .setStartOffset(Audio.samplingStart(), TimeUnit.SECONDS)
                .setVerbosity(FFmpegBuilder.Verbosity.INFO)
                .addOutput("-")
                .setFormat("null")
                .setDuration(Audio.samplingDuration(), TimeUnit.SECONDS)
                .setComplexVideoFilter("silencedetect=noise=-40dB:d=5")
                .done();

        ConsoleOutputCapturer consoleOutputCapturer = new ConsoleOutputCapturer();

        consoleOutputCapturer.start();
        Audio.ffMpegExecutor().createJob(ffMpegBuilder).run();
        String ffMpegOutput = consoleOutputCapturer.stop();

        return new SilenceDetectionResult(silenceDetections(ffMpegOutput));
    }

    private static List<SilenceDetection> silenceDetections(String ffMpegOutput) {
        Scanner scanner = new Scanner(ffMpegOutput);

        String floatingPointNumberRegex = "\\s-?\\d+(\\.\\d+)?";
        String silenceStartMatch;
        String silenceDurationMatch = null;
        Duration silenceStartTimestamp;
        Duration silenceDuration;
        List<SilenceDetection> silenceDetections = new ArrayList<>();

        while(scanner.hasNextLine()) {
            silenceStartMatch = scanner.findInLine("silence_start:" + floatingPointNumberRegex);
            if( silenceStartMatch != null) {


                while(silenceDurationMatch == null && scanner.hasNextLine()) {
                    scanner.nextLine();
                    silenceDurationMatch = scanner.findInLine("silence_duration:" + floatingPointNumberRegex);
                }

                silenceStartTimestamp = durationFromSilenceDetectionMatch(silenceStartMatch);
                if(silenceDurationMatch == null) {
                    //TODO: fill duration (of last unclosed silence in video) during creation of final json output
                    silenceDuration = Duration.ZERO;
                }
                else {
                    silenceDuration = durationFromSilenceDetectionMatch(silenceDurationMatch);
                }

                silenceDetections.add(new SilenceDetection(silenceStartTimestamp, silenceDuration));
            }

            silenceStartMatch = null;
            silenceDurationMatch = null;

            if(scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        return silenceDetections;
    }

    private static Duration durationFromSilenceDetectionMatch(String match) {
        Scanner scanner = new Scanner(match);
        scanner.useLocale(Locale.US);
        double seconds = -1;

        while (scanner.hasNext()) {
            if(scanner.hasNextDouble()) {
                seconds = scanner.nextDouble();
                break;
            }
            scanner.next();
        }

        return Duration.ofSeconds((long) seconds);
    }
}
