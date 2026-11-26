package com.example.dotechuser.Model;

public class Notification {
    private String userId;
    private String date;
    private String currentDate;
    private String time;
    private String currentTime;
    private String userName;
    private String userProfile;
    private String consultId;
    private String consultType;
    private String status;

    public Notification(String userId, String date, String currentDate, String time, String currentTime, String userName, String userProfile, String consultType, String status) {
        this.userId = userId;
        this.date = date;
        this.currentDate = currentDate;
        this.time = time;
        this.currentTime = currentTime;
        this.userName = userName;
        this.userProfile = userProfile;
        this.consultType = consultType;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getConsultId() {
        return consultId;
    }

    public void setConsultId(String consultId) {
        this.consultId = consultId;
    }

    public String getConsultType() {
        return consultType;
    }

    public void setConsultType(String consultType) {
        this.consultType = consultType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
