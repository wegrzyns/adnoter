package cea.video.model;

import javafx.util.Pair;

import java.util.*;

public class Chunk implements AutoCloseable{

    public static final int FRAME_MATCH_NOT_COMPUTED = -1;
    public static final int FRAME_0_1_MATCH_INDEX = 0;
    public static final int FRAME_1_2_MATCH_INDEX = 1;
    public static final int FRAME_0_2_MATCH_INDEX = 2;

    private Frame firstFrame;
    private Frame middleFrame;
    private Frame lastFrame;
    private List<Integer> frameMatches;
    private static Map<Integer, Pair<Integer, Integer>> frameMatchesIndexes;

    static {
        frameMatchesIndexes = new HashMap<>();
        frameMatchesIndexes.put(FRAME_0_1_MATCH_INDEX, new Pair<>(0, 1));
        frameMatchesIndexes.put(FRAME_0_2_MATCH_INDEX, new Pair<>(0, 2));
        frameMatchesIndexes.put(FRAME_1_2_MATCH_INDEX, new Pair<>(1, 2));
    }

    public Chunk(Frame firstFrame, Frame middleFrame, Frame lastFrame) {
        this.firstFrame = firstFrame;
        this.middleFrame = middleFrame;
        this.lastFrame = lastFrame;

        Integer[] arr = new Integer[3];
        Arrays.fill(arr, FRAME_MATCH_NOT_COMPUTED);
        this.frameMatches = Arrays.asList(arr);
    }

    public List<Frame> framesAsList() {
        List<Frame> toRet = new ArrayList<>();
        toRet.add(firstFrame);
        toRet.add(middleFrame);
        toRet.add(lastFrame);

        return toRet;
    }

    public boolean frameMatchesNotComputed() {
        return frameMatches.stream().anyMatch(integer -> integer == FRAME_MATCH_NOT_COMPUTED);
    }

    @Override
    public void close() throws Exception {
        for (Frame frame: framesAsList()) {
            if(frame.getFrame() != null) {
                frame.close();
            }
        }
    }

    public int getFrameMatch(int index) {
        return frameMatches.get(index);
    }

    public void setFrameMatch(int index, int value) {
        frameMatches.set(index, value);
    }

    public static Pair<Integer, Integer> getFrameMatchIndex(int matchIndex) {
        return frameMatchesIndexes.get(matchIndex);
    }

    public Frame getFirstFrame() {
        return firstFrame;
    }

    public void setFirstFrame(Frame firstFrame) {
        this.firstFrame = firstFrame;
    }

    public Frame getMiddleFrame() {
        return middleFrame;
    }

    public void setMiddleFrame(Frame middleFrame) {
        this.middleFrame = middleFrame;
    }

    public Frame getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(Frame lastFrame) {
        this.lastFrame = lastFrame;
    }

    public List<Integer> getFrameMatches() {
        return frameMatches;
    }

    public void setFrameMatches(List<Integer> frameMatches) {
        this.frameMatches = frameMatches;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "firstFrame=" + firstFrame +
                ", middleFrame=" + middleFrame +
                ", lastFrame=" + lastFrame +
                ", frameMatches=" + frameMatches +
                '}';
    }
}
