package com.example.nju_tube.ui;

public class CommentItem {
    private final int id;
    private final String commentContent;
    private final int commentUserId;
    private final String commentUserName;
    private final String commentTimeStamp;

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public String getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public CommentItem(int id, String commentContent, String commentUserName, String commentTimeStamp, int commentUserId) {
        this.id = id;
        this.commentContent = commentContent;
        this.commentTimeStamp = commentTimeStamp;
        this.commentUserName = commentUserName;
        this.commentUserId = commentUserId;
    }

    public int getId() {
        return id;
    }

    public int getCommentUserId() {
        return commentUserId;
    }
}
