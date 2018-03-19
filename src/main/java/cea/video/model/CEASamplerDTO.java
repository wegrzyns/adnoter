package cea.video.model;

import java.time.Duration;

public class CEASamplerDTO {

    private static final int DEFAULT_SAMPLING_START = 0;
    private static final int DEFAULT_SAMPLING_END = -1;

    private CEAVideo video;
    private Duration chunkLength;
    private Duration samplingStart;
    private Duration samplingEnd;

    public CEASamplerDTO(CEAVideo video, Duration chunkLength) {
        this.video = video;
        this.chunkLength = chunkLength;
        this.samplingStart = Duration.ofSeconds(DEFAULT_SAMPLING_START);
        this.samplingEnd = Duration.ofSeconds(DEFAULT_SAMPLING_END);
    }

    public long getChunkLenghtInSeconds() {
        return chunkLength.getSeconds();
    }

    public long startFrameOffset() {
        return (long) (video.getFrameRate()*samplingStart.getSeconds());
    }

    public long endFrameOffset() {
        if(samplingEnd.getSeconds() == DEFAULT_SAMPLING_END) {
            return video.getFrameCount();
        }
        return (long) (video.getFrameRate()*samplingEnd.getSeconds());
    }

    public CEAVideo getVideo() {
        return video;
    }

    public void setVideo(CEAVideo video) {
        this.video = video;
    }

    public Duration getChunkLength() {
        return chunkLength;
    }

    public void setChunkLength(Duration chunkLength) {
        this.chunkLength = chunkLength;
    }

    public Duration getSamplingStart() {
        return samplingStart;
    }

    public void setSamplingStart(Duration samplingStart) {
        this.samplingStart = samplingStart;
    }

    public Duration getSamplingEnd() {
        return samplingEnd;
    }

    public void setSamplingEnd(Duration samplingEnd) {
        this.samplingEnd = samplingEnd;
    }
}
