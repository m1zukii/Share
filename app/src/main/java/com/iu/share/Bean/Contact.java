package com.iu.share.Bean;

import org.litepal.crud.LitePalSupport;

public class Contact extends LitePalSupport {
    private String name;
    private String phoneNumber;
    private String username;

    public Contact(String name, String phoneNumber, String username) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
