package com.iu.share.Bean;

import org.litepal.crud.LitePalSupport;

public class User  extends LitePalSupport {
    String username;
    String password;
    String phoneNumber;
    String lastMessage;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String phoneNumber, String lastMessage) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.lastMessage = lastMessage;
        this.password = "";
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                '}';
    }
}
