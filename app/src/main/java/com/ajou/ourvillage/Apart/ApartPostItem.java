package com.ajou.ourvillage.Apart;

public class ApartPostItem {
    private int img_profile; // 글 작성자의 프로필 이미지
    private String writer; // 글 작성자
    private String date; // 글 작성 시간
    private String title; // 글 제목
    private String content; // 글 내용
    private String likeCnt; // 좋아요 갯수
    private String commentCount; // 댓글 갯수
    private boolean like; // 좋아요 클릭 여부

    public ApartPostItem(int img_profile, String writer, String date, String title, String content, String likeCnt, String commentCount, boolean like) {
        this.img_profile = img_profile;
        this.writer = writer;
        this.date = date;
        this.title = title;
        this.content = content;
        this.likeCnt = likeCnt;
        this.commentCount = commentCount;
        this.like = like;
    }

    public int getImg_profile() {
        return img_profile;
    }

    public void setImg_profile(int img_profile) {
        this.img_profile = img_profile;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(String likeCnt) {
        this.likeCnt = likeCnt;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
