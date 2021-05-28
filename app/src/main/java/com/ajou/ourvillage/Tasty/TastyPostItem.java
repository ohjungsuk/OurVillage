package com.ajou.ourvillage.Tasty;

public class TastyPostItem {
//    private String img_profile; // 글 작성자의 프로필 이미지
    private String img_content; // 사진..
    private String writer; // 글 작성자
    private String date; // 글 작성 시간
    private String title; // 글 제목
    private String content; // 글 내용
    private String likeCnt; // 좋아요 갯수
    private String commentCount; // 댓글 갯수

    public TastyPostItem(String img_content, String writer, String date, String title, String content, String likeCnt, String commentCount) {
//        this.img_profile = img_profile;
        this.img_content = img_content;
        this.writer = writer;
        this.date = date;
        this.title = title;
        this.content = content;
        this.likeCnt = likeCnt;
        this.commentCount = commentCount;
    }

//    public String getImg_profile() {
//        return img_profile;
//    }
//
//    public void setImg_profile(String img_profile) {
//        this.img_profile = img_profile;
//    }

    public String getImg_content() {
        return img_content;
    }

    public void setImg_content(String img_content) {
        this.img_content = img_content;
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

}
