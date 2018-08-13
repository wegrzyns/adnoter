package cea.video.frame_transition_detector;

import cea.video.model.Chunk;
import cea.video.model.Frame;
import cea.video.model.Video;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.stream.Stream;

public class BlackFramesTransitionDetector extends DefaultTransitionManager {

    public static final double blackFrameThreshold = 0.95;
    private double nonBlackPixelsThreshold;

    public BlackFramesTransitionDetector(Video video) {
        this.nonBlackPixelsThreshold = video.getFrameArea() * (1 - blackFrameThreshold);
    }

    @Override
    protected boolean transitionRightChunk(Chunk computedChunk) {
        Frame middleFrame = computedChunk.getMiddleFrame();
        Frame lastFrame = computedChunk.getLastFrame();

        return blackFramesTransition(middleFrame, lastFrame, computedChunk);
    }

    @Override
    protected boolean transitionLeftChunk(Chunk computedChunk) {
        Frame firstFrame = computedChunk.getFirstFrame();
        Frame middleFrame = computedChunk.getMiddleFrame();

        return blackFramesTransition(firstFrame, middleFrame, computedChunk);
    }

    private boolean blackFramesTransition(Frame frame1, Frame frame2, Chunk chunk) {
        Mat mat1 = frame1.getFrame().clone();
        Mat mat2 = frame2.getFrame().clone();


        Imgproc.cvtColor(mat1, mat1, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(mat2, mat2, Imgproc.COLOR_RGB2GRAY);

        int frame1NonBlackPixelsCount = Core.countNonZero(mat1);
        int frame2NonBlackPixelsCount = Core.countNonZero(mat2);

        mat1.release();
        mat2.release();

        Stream<Integer> pomStream1 = Stream.of(frame1NonBlackPixelsCount, frame2NonBlackPixelsCount);
        Stream<Integer> pomStream2 = Stream.of(frame1NonBlackPixelsCount, frame2NonBlackPixelsCount);

        if(pomStream1.anyMatch(pixelCount -> pixelCount < nonBlackPixelsThreshold) && pomStream2.anyMatch(pixelCount -> pixelCount > nonBlackPixelsThreshold)) {
            chunk.setBlackFramesTransition(true);
            return true;
        }

        return false;
    }


}
