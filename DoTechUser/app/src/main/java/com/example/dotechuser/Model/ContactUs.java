package com.example.dotechuser.Model;

public class ContactUs {
    private String userId;
    private String profileImage;
    private String name;
    private String email;
    private String mobile;
    private String subject;
    private String message;
    private String currentDate;
    private String currentTime;
    private String status;

    public ContactUs(String userId, String profileImage, String name, String email, String mobile, String subject, String message, String currentDate, String currentTime, String status) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.subject = subject;
        this.message = message;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
