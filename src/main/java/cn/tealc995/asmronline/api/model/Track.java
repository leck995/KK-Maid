package cn.tealc995.asmronline.api.model;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:07
 */
public class Track {
    private String title;
    private String type;
    private String workTitle;

    private double duration;

    private String hash;
    private String mediaDownloadUrl;
    private String mediaStreamUrl;
    private String streamLowQualityUrl;
    private List<Track> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMediaDownloadUrl() {
        return mediaDownloadUrl;
    }

    public void setMediaDownloadUrl(String mediaDownloadUrl) {
        this.mediaDownloadUrl = mediaDownloadUrl;
    }

    public String getMediaStreamUrl() {
        return mediaStreamUrl;
    }

    public void setMediaStreamUrl(String mediaStreamUrl) {
        this.mediaStreamUrl = mediaStreamUrl;
    }

    public String getStreamLowQualityUrl() {
        return streamLowQualityUrl;
    }

    public void setStreamLowQualityUrl(String streamLowQualityUrl) {
        this.streamLowQualityUrl = streamLowQualityUrl;
    }

    public List<Track> getChildren() {
        return children;
    }

    public void setChildren(List<Track> children) {
        this.children = children;
    }
}