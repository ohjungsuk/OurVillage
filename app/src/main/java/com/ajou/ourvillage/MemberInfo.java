package com.ajou.ourvillage;

public class MemberInfo {
    private String nickname;
    private String name;
    private String phone_num;
    private String address;
    private String apart;

    public MemberInfo(String nickname, String name, String phone_num, String address, String apart) {
        this.nickname = nickname;
        this.name = name;
        this.phone_num = phone_num;
        this.address = address;
        this.apart = apart;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApart() {
        return apart;
    }

    public void setApart(String apart) {
        this.apart = apart;
    }
}
