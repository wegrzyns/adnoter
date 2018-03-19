package cea.legacyvideo;

public class AdnoterChunk {
    private AdnFrame frameFirst;
    private AdnFrame frameMiddle;
    private AdnFrame frameLast;

    public AdnoterChunk() {
    }

    public AdnoterChunk(AdnFrame frameFirst, AdnFrame frameMiddle, AdnFrame frameLast) {
        this.frameFirst = frameFirst;
        this.frameMiddle = frameMiddle;
        this.frameLast = frameLast;
    }

    public AdnFrame getFrameFirst() {
        return frameFirst;
    }

    public void setFrameFirst(AdnFrame frameFirst) {
        this.frameFirst = frameFirst;
    }

    public AdnFrame getFrameMiddle() {
        return frameMiddle;
    }

    public void setFrameMiddle(AdnFrame frameMiddle) {
        this.frameMiddle = frameMiddle;
    }

    public AdnFrame getFrameLast() {
        return frameLast;
    }

    public void setFrameLast(AdnFrame frameLast) {
        this.frameLast = frameLast;
    }
}
