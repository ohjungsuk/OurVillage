package com.ajou.ourvillage.Main;

public class WriteCommentInfo {
    private String feed_id;
    private String writer;
    private String comment;
    private String date;
    private String comment_uid;

    public WriteCommentInfo(String feed_id, String writer, String comment, String date) {
        this.feed_id = feed_id;
        this.writer = writer;
        this.comment = comment;
        this.date = date;
    }

    public WriteCommentInfo(String feed_id, String writer, String comment, String date,String comment_uid) {
        this.feed_id = feed_id;
        this.writer = writer;
        this.comment = comment;
        this.date = date;
        this.comment_uid = comment_uid;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }
}
