package com.ajou.ourvillage.Tasty;

public class TastyPostItem {
    private String writer; // 글 작성자
    private String date; // 글 작성 시간 (방문 시간)
    private String address; // 음식점 주소
    private String rate; // 평점
    private String review; // 한줄평
    private String recommend; // 추천음식
    private String foodImage; // 음식 사진
    private String latitude;
    private String longitude;

    public TastyPostItem(String writer, String date, String address, String rate, String review, String recommend, String foodImage, String latitude, String longitude) {
        this.writer = writer;
        this.date = date;
        this.address = address;
        this.rate = rate;
        this.review = review;
        this.recommend = recommend;
        this.foodImage = foodImage;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
