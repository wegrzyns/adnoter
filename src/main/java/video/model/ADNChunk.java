package video.model;

public class ADNChunk {

    private ADNFrame firstFrame;
    private ADNFrame middleFrame;
    private ADNFrame lastFrame;

    public ADNChunk(ADNFrame firstFrame, ADNFrame middleFrame, ADNFrame lastFrame) {
        this.firstFrame = firstFrame;
        this.middleFrame = middleFrame;
        this.lastFrame = lastFrame;
    }

    public ADNFrame getFirstFrame() {
        return firstFrame;
    }

    public void setFirstFrame(ADNFrame firstFrame) {
        this.firstFrame = firstFrame;
    }

    public ADNFrame getMiddleFrame() {
        return middleFrame;
    }

    public void setMiddleFrame(ADNFrame middleFrame) {
        this.middleFrame = middleFrame;
    }

    public ADNFrame getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(ADNFrame lastFrame) {
        this.lastFrame = lastFrame;
    }
}
