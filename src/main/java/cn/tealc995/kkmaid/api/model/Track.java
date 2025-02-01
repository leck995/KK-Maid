package cn.tealc995.kkmaid.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:07
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFolder(){
        return type.equals("folder");
    }
}