package com.example.doctorappui.modal;

public
class ContactUs {
    private String userId,name,email,mobile,subject,message,profileImage,currentDate,status;
    private long timeAt;
    public
    ContactUs(String userId, String name, String email, String mobile, String subject, String message, String profileImage,long timeAt, String currentDate, String status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.subject = subject;
        this.message = message;
        this.profileImage = profileImage;
        this.timeAt = timeAt;
        this.currentDate = currentDate;
        this.status = status;
    }

    public
    ContactUs() {
    }

    public
    String getCurrentDate() {
        return currentDate;
    }

    public
    void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public
    String getProfileImage() {
        return profileImage;
    }

    public
    void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public
    String getUserId() {
        return userId;
    }

    public
    void setUserId(String userId) {
        this.userId = userId;
    }

    public
    String getName() {
        return name;
    }

    public
    void setName(String name) {
        this.name = name;
    }

    public
    String getEmail() {
        return email;
    }

    public
    void setEmail(String email) {
        this.email = email;
    }

    public
    String getMobile() {
        return mobile;
    }

    public
    void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public
    String getSubject() {
        return subject;
    }

    public
    void setSubject(String subject) {
        this.subject = subject;
    }

    public
    String getMessage() {
        return message;
    }

    public
    void setMessage(String message) {
        this.message = message;
    }

    public
    long getTimeAt() {
        return timeAt;
    }

    public
    void setTimeAt(long timeAt) {
        this.timeAt = timeAt;
    }

    public
    String getStatus() {
        return status;
    }

    public
    void setStatus(String status) {
        this.status = status;
    }
}
