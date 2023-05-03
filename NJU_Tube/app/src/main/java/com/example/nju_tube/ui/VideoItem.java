package com.example.nju_tube.ui;

/** 视频信息对象，用于填充列表，包含视频的粗略信息 */

public class VideoItem {
    public VideoItem(String videoTitle, String uploader, String date) {
        this.videoTitle = videoTitle;
        this.uploader = uploader;
        this.date = date;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    String videoTitle = "test";

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String uploader = "test";
    String date = "test";
    String url = "secret";
}
