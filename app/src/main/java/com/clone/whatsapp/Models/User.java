package com.clone.whatsapp.Models;

public class User {

   private String UserID;
   private String UserName;
   private String UserPhone;
   private String ImageUrl;
   private String Email;
   private String Gender;
   private String Status;
   private String Bio;



   public User(){

   }
   public User(String userID, String userName, String userPhone, String imageUrl, String email, String gender, String status, String bio) {
        UserID = userID;
        UserName = userName;
        UserPhone = userPhone;
        ImageUrl = imageUrl;
        Email = email;
        Gender = gender;
        Status = status;
        Bio = bio;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getUserID() {
        return UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getEmail() {
        return Email;
    }

    public String getGender() {
        return Gender;
    }

    public String getStatus() {
        return Status;
    }

    public String getBio() {
        return Bio;
    }
}
