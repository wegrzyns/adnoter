package cea.audio.parser;

import cea.Util.ConfigurationUtil;
import cea.Util.ConsoleOutputCapturer;
import cea.audio.model.SilenceDetection;
import cea.audio.model.SilenceDetectionResult;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Audio {

    //TODO: move to configuration file(which wont be commited)
    public static final String FFMPEG_PATH = "E:\\projekty\\mag\\ffmpeg-20171014-0655810-win64-static\\bin\\ffmpeg.exe";
    public static final String FFPROBE_PATH = "E:\\projekty\\mag\\ffmpeg-20171014-0655810-win64-static\\bin\\ffprobe.exe";

    private static final int SAMPLING_START_MINUTES = ConfigurationUtil.configuration().getInt("sampling.startMinutes", 0);
    private static final int SAMPLING_START_SECONDS = ConfigurationUtil.configuration().getInt("sampling.startSeconds", 0);
    private static final int SAMPLING_END_MINUTES = ConfigurationUtil.configuration().getInt("sampling.endMinutes", 999);
    private static final int SAMPLING_END_SECONDS = ConfigurationUtil.configuration().getInt("sampling.endSeconds", 0);

    private String fileToParsePath;

    public Audio(String filePath) {
        fileToParsePath = filePath;
    }

    public void parseAudio() throws IOException {
        FFmpegBuilder ffMpegBuilder = new FFmpegBuilder();

        ffMpegBuilder
                .setInput(fileToParsePath)
                .setStartOffset(samplingStart(), TimeUnit.SECONDS)
                .overrideOutputFiles(true)
                .addOutput("test.wav")
                .setDuration(samplingDuration(), TimeUnit.SECONDS)
                .setAudioChannels(1)
                .setAudioSampleRate(16000)
                .setAudioCodec("pcm_s16le")
                .setFormat("wav")
                .done();

        FFmpegExecutor ffMpegExecutor = ffMpegExecutor();

        ffMpegExecutor.createJob(ffMpegBuilder).run();
    }

    public static FFmpegExecutor ffMpegExecutor() throws IOException {
        FFmpeg ffMpeg = new FFmpeg(FFMPEG_PATH);
        return new FFmpegExecutor(ffMpeg);
    }

    public static long samplingStart() {
        return SAMPLING_START_MINUTES * 60 + SAMPLING_START_SECONDS;
    }

    public static long samplingDuration() {
        return (SAMPLING_END_MINUTES * 60 + SAMPLING_END_SECONDS) - samplingStart();
    }
}
