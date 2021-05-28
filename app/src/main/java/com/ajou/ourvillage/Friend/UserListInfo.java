package com.ajou.ourvillage.Friend;

public class UserListInfo {
    //private int friend_img_profile;
    private String nickname;
    private String address;

    public UserListInfo(String nickname, String address) {
        //this.friend_img_profile = friend_img_profile;
        this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
