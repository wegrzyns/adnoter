package video.model;

public class CEAChunk {

    private CEAFrame firstFrame;
    private CEAFrame middleFrame;
    private CEAFrame lastFrame;

    public CEAChunk(CEAFrame firstFrame, CEAFrame middleFrame, CEAFrame lastFrame) {
        this.firstFrame = firstFrame;
        this.middleFrame = middleFrame;
        this.lastFrame = lastFrame;
    }

    public CEAFrame getFirstFrame() {
        return firstFrame;
    }

    public void setFirstFrame(CEAFrame firstFrame) {
        this.firstFrame = firstFrame;
    }

    public CEAFrame getMiddleFrame() {
        return middleFrame;
    }

    public void setMiddleFrame(CEAFrame middleFrame) {
        this.middleFrame = middleFrame;
    }

    public CEAFrame getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(CEAFrame lastFrame) {
        this.lastFrame = lastFrame;
    }
}
