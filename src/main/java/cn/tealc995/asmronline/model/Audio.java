package cn.tealc995.asmronline.model;

import cn.tealc995.asmronline.api.model.Track;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-14 00:08
 */
public class Audio {
    private String title;
    private String downloadUrl;
    private String streamUrl;
    private String streamLowQualityUrl;
    private double duration;


    public Audio() {
    }

    public Audio(String title, String downloadUrl) {
        this.title = title;
        this.downloadUrl = downloadUrl;
    }

    public Audio(String title, String downloadUrl, String streamUrl, String streamLowQualityUrl, double duration) {
        this.title = title;
        this.downloadUrl = downloadUrl;
        this.streamUrl = streamUrl;
        this.streamLowQualityUrl = streamLowQualityUrl;
        this.duration = duration;
    }

    public String getTitleWithoutSuffix() {
        int index=title.lastIndexOf(".");
        if (index != -1)
            return title.substring(0,index);
        else
            return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamLowQualityUrl() {
        return streamLowQualityUrl;
    }

    public void setStreamLowQualityUrl(String streamLowQualityUrl) {
        this.streamLowQualityUrl = streamLowQualityUrl;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}