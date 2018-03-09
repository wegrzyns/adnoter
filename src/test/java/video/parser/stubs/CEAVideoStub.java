package video.parser.stubs;

import org.opencv.core.Mat;
import video.model.CEAFrame;
import video.model.CEAVideo;

import java.time.Duration;

public class CEAVideoStub extends CEAVideo {

    @Override
    public CEAFrame getFrame(long position) {
        return new CEAFrame(new Mat(), position, Duration.ofSeconds((long) (position/getFrameRate())), new CEAVideo());
    }
}
