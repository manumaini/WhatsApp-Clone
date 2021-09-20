package com.clone.whatsapp.Models;

public class ChatContact {

    private String name;
    private String lastMSG;
    private String imageUrl;
    private String userID;
    private int chatNumber;

    public ChatContact(String name, String lastMSG, String imageUrl,String userID) {
        this.name = name;
        this.lastMSG = lastMSG;
        this.imageUrl = imageUrl;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ChatContact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMSG() {
        return lastMSG;
    }

    public void setLastMSG(String lastMSG) {
        this.lastMSG = lastMSG;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
