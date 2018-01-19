package parser;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import speech.SphinxDemo;

import java.io.File;
import java.io.IOException;

public class Audio {

    //TODO: Enable spring support
    //TODO: move to configuration file(which wont be commited)
    public static final String FFMPEG_PATH = "E:\\projekty\\mag\\ffmpeg-20171014-0655810-win64-static\\bin\\ffmpeg.exe";
    public static final String FFPROBE_PATH = "E:\\projekty\\mag\\ffmpeg-20171014-0655810-win64-static\\bin\\ffprobe.exe";

    private String fileToParsePath;

    public Audio(String filePath) {
        fileToParsePath = filePath;
    }

    public void parseAudio() throws IOException {
        FFmpeg ffMpeg = new FFmpeg(FFMPEG_PATH);
        FFprobe ffProbe = new FFprobe(FFPROBE_PATH);
        FFmpegBuilder ffMpegBuilder = new FFmpegBuilder();

        ffMpegBuilder
                .setInput(fileToParsePath)
                .overrideOutputFiles(true)
                .addOutput("test.wav")
                .setAudioChannels(1)
                .setAudioSampleRate(16000)
                .setAudioCodec("pcm_s16le")
                .setFormat("wav")
                .done();

        FFmpegExecutor ffMpegExecutor = new FFmpegExecutor(ffMpeg);

        ffMpegExecutor.createJob(ffMpegBuilder).run();

        //SphinxDemo.transcribe("test.wav");
        SphinxDemo.detectVoiceActivity("test.wav");
    }
}
