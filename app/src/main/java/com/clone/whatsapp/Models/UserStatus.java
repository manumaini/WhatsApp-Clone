package com.clone.whatsapp.Models;

public class UserStatus {

    private String UserName;
    private String DateTime;
    private int StatusCount;
    private String ThumbnailUrl;

    public UserStatus(String userName, String dateTime, int statusCount, String thumbnailUrl) {
        UserName = userName;
        DateTime = dateTime;
        StatusCount = statusCount;
        ThumbnailUrl = thumbnailUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public int getStatusCount() {
        return StatusCount;
    }

    public void setStatusCount(int statusCount) {
        StatusCount = statusCount;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }
}
