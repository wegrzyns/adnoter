package cea.video.input.stubs;

import cea.video.model.Frame;
import org.opencv.core.Mat;
import cea.video.model.Video;

import java.time.Duration;

public class VideoStub extends Video {

    @Override
    public Frame getFrame(long position) {
        return new Frame(new Mat(), position, Duration.ofSeconds((long) (position/getFrameRate())), new Video());
    }
}
