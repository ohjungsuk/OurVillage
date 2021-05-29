package com.ajou.ourvillage.Friend;

public class FriendListInfo {
    //private int friend_img_profile;
    private String my_nickname;
    private String friend_nickname;
    private String address;

    public FriendListInfo(String my_nickname, String friend_nickname, String address) {
        //this.friend_img_profile = friend_img_profile;
        this.my_nickname = my_nickname;
        this.friend_nickname = friend_nickname;
        this.address = address;
    }
//
//    public Integer getFriend_img_profile() {
//        return friend_img_profile;
//    }
//
//    public void setFriend_img_profile(Integer friend_img_profile) {
//        this.friend_img_profile = friend_img_profile;
//    }


    public String getMy_nickname() {
        return my_nickname;
    }

    public void setMy_nickname(String my_nickname) {
        this.my_nickname = my_nickname;
    }

    public String getFriend_nickname() {
        return friend_nickname;
    }

    public void setFriend_nickname(String friend_nickname) {
        this.friend_nickname = friend_nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
