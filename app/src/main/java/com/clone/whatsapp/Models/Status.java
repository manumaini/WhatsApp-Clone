package com.clone.whatsapp.Models;

import java.io.Serializable;

public class Status implements Serializable {

    private String Name;
    private String Caption;
    private String DateTime;
    private String UserId;
    private String ThumbnailUrl;



    public Status(String name, String caption, String dateTime, String userId, String thumbnailUrl) {
        Name = name;
        Caption = caption;
        DateTime = dateTime;
        UserId = userId;
        ThumbnailUrl = thumbnailUrl;
    }

    public Status() {
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


}
