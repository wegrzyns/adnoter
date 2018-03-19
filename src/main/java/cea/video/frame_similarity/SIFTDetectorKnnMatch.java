package cea.video.frame_similarity;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.BFMatcher;

import java.util.ArrayList;
import java.util.List;

public class SIFTDetectorKnnMatch extends SIFTDetector {

    @Override
    protected int matchFramesDescriptors(Mat frame1KeyPointDescriptors, Mat frame2KeyPointDescriptors) {
        List<MatOfDMatch> descriptorMatches = new ArrayList<>();
        BFMatcher bfMatcher = BFMatcher.create(BFMatcher.BRUTEFORCE, false);
        bfMatcher.knnMatch(frame1KeyPointDescriptors, frame2KeyPointDescriptors, descriptorMatches, 2);

        return descriptorMatches.stream()
                .filter(match -> match.toList().get(0).distance < 0.8 * match.toList().get(1).distance)
                .mapToInt(match -> 1)
                .sum();
    }
}
