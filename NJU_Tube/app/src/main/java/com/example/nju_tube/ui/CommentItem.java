package com.example.nju_tube.ui;

public class CommentItem {
    private final String commentContent;
    private final String commentUserID;
    private final String commentTimeStamp;

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentUserID() {
        return commentUserID;
    }

    public String getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public CommentItem(String commentContent, String commentUserID, String commentTimeStamp) {
        this.commentContent = commentContent;
        this.commentTimeStamp = commentTimeStamp;
        this.commentUserID = commentUserID;
    }
}
