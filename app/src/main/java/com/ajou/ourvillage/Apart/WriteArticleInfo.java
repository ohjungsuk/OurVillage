package com.ajou.ourvillage.Apart;

public class WriteArticleInfo {
    private String title;
    private String content;
    private String time;
    private String writer;
    private int likeCount;
    private int commentCount;

    public WriteArticleInfo(String title, String content, String time, String writer, int likeCount, int commentCount) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.writer = writer;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
