package com.example.doctorappui.modal;

public
class DepartmentDoctor {
    private String degreeImage,profileImage,name,emailId,mobile_number,gender,userId,category;
    private long timeAt;

    public
    DepartmentDoctor() {
    }

    public
    String getCategory() {
        return category;
    }

    public
    void setCategory(String category) {
        this.category = category;
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
    String getDegreeImage() {
        return degreeImage;
    }

    public
    void setDegreeImage(String degreeImage) {
        this.degreeImage = degreeImage;
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
    String getName() {
        return name;
    }

    public
    void setName(String name) {
        this.name = name;
    }

    public
    String getEmailId() {
        return emailId;
    }

    public
    void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public
    String getMobile_number() {
        return mobile_number;
    }

    public
    void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public
    String getGender() {
        return gender;
    }

    public
    void setGender(String gender) {
        this.gender = gender;
    }

    public
    String getUserId() {
        return userId;
    }

    public
    void setUserId(String userId) {
        this.userId = userId;
    }
}
