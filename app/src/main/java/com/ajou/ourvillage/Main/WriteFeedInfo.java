package com.ajou.ourvillage.Main;

public class WriteFeedInfo {
    private String title;
    private String comment;
    private String creater;

    public WriteFeedInfo(String title, String comment,String creater) {
        this.title = title;
        this.comment = comment;
        this.creater = creater;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
