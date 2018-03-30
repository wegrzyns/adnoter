package cea.video.frame_similarity;

import cea.video.model.CEAChunk;
import cea.video.model.CEAFrame;
import cea.video.model.CEASlideRegion;
import cea.video.slide_region.DefaultManager;
import cea.video.slide_region.SlideRegionManager;
import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SIFTDetector implements FrameSimilarityDetector {

    private SIFT sift;

    public SIFTDetector() {
        sift = SIFT.create();
    }

    @Override
    public CEAChunk computeChunkFramesSimiliarity(CEAChunk chunk) {
        SlideRegionManager slideRegionManager = new DefaultManager();
        List<CEAFrame> chunkFrames = chunk.framesAsList();

        List<Mat> framesKeyPointsDescriptors = chunkFrames.stream()
                .map(frame -> {
                    if(frame.isSIFTcomputed()) return frame.getKeyPointDescriptors();
                    CEASlideRegion slideRegion = slideRegionManager.getSlideRegion(frame);
                    if(slideRegion == null) return null;
                    Mat slideRegionMask = slideRegion.getSlideRegionMask();
                    Mat preparedFrame = prepareFrame(frame);
                    MatOfKeyPoint keyPoints = detectFeatures(
                            preparedFrame,
                            slideRegionMask);
                    //-------- Visualization
//                    ImageUtil.drawContoursAndKeypoints(frame.getFrame(), false, null, keyPoints);
//                    ImageUtil.showResult(frame.getFrame(), frame.getTimestamp());
                    //-------- Visualization
                    frame.setKeyPointDescriptors(keyPointDescriptors(preparedFrame, keyPoints));
                    frame.setSIFTcomputed(true);
                    return keyPointDescriptors(preparedFrame, keyPoints);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        if(missingSlideRegion(framesKeyPointsDescriptors)) return chunk;

        IntStream.rangeClosed(0, 2)
                .filter(i -> frameMatchNotComputed(chunk.getFrameMatch(i)))
                .forEach(i -> chunk.setFrameMatch(i, matchFramesDescriptors(i, framesKeyPointsDescriptors)));

        return chunk;
    }

    private boolean missingSlideRegion(List<Mat> framesKeyPointsDescriptors) {
        return framesKeyPointsDescriptors.size() < 3;
    }

    private Mat prepareFrame(CEAFrame ceaframe) {
        Mat frame = ceaframe.getFrame().clone();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(frame, frame,  new Size(21, 21), 0);
        return frame;
    }

    private MatOfKeyPoint detectFeatures(Mat frame, Mat frameRegionMask) {
        MatOfKeyPoint toRet = new MatOfKeyPoint();
        sift.detect(frame, toRet, frameRegionMask);
        return toRet;
    }

    private Mat keyPointDescriptors(Mat frame, MatOfKeyPoint siftFeatures) {
        Mat keyPointsDescriptors = new Mat();
        sift.compute(frame, siftFeatures, keyPointsDescriptors);
        return keyPointsDescriptors;
    }

    private int matchFramesDescriptors(int matchIndex, List<Mat> descriptors) {
        Pair<Integer, Integer> indexes = CEAChunk.getFrameMatchIndex(matchIndex);

        Mat descriptor1 = descriptors.get(indexes.getKey());
        Mat descriptor2 = descriptors.get(indexes.getValue());

        return matchFramesDescriptors(descriptor1, descriptor2);
    }

    protected int matchFramesDescriptors(Mat frame1KeyPointDescriptors, Mat frame2KeyPointDescriptors) {
        MatOfDMatch descriptorMatches = new MatOfDMatch();
        BFMatcher bfMatcher = BFMatcher.create(BFMatcher.BRUTEFORCE, true);
        bfMatcher.match(frame1KeyPointDescriptors, frame2KeyPointDescriptors, descriptorMatches);

        return descriptorMatches.toArray().length;
    }

    private boolean frameMatchNotComputed(int frameMatchValue) {
        return frameMatchValue == CEAChunk.FRAME_MATCH_NOT_COMPUTED;
    }
}
