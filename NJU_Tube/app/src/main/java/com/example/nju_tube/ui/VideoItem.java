package com.example.nju_tube.ui;

/**
 * 视频信息对象，用于填充列表，包含视频的粗略信息
 */

public class VideoItem {
    private final int id;
    private final String videoUrl;
    private final String coverUrl;
    private final String videoTitle;
    private final UserItem author;
    private final String uploadDate;
    private int favoriteCount;
    private int commentCount;
    private boolean isFavorite;

    public VideoItem(int id, String videoUrl, String coverUrl, String videoTitle, UserItem author,
                     String uploadDate, int favoriteCount, int commentCount, boolean isFavorite) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.coverUrl = coverUrl;
        this.videoTitle = videoTitle;
        this.author = author;
        this.uploadDate = uploadDate;

        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public UserItem getAuthor() {
        return author;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
