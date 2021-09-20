package com.clone.whatsapp.Models;

public class Contact {

    private String name;
    private String desc;
    private String imageUrl;
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Contact(String name, String desc, String imageUrl , String userID) {
        this.name = name;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.userID = userID;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
