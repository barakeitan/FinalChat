package com.barak.eitan.firebasechat;

/**
 * Created by barak on 24/03/2017.
 */

public class UserInfo {
    private String email;
    private String phone;
    private String birthday;
    private boolean isOnline;
    private String dispName;

    public UserInfo(){

    }

    public UserInfo(String email, String phone, String birthday, String dispName) {
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.isOnline = false;
        this.dispName = dispName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }
}
