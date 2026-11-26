package com.example.doctorappui.modal;

public
class SystemUsers {

    private String birthDate,bloodGroup,email,gender,height,mobile,name,password,profileImage,status,weight,userId;

    public
    SystemUsers(String birthDate, String bloodGroup, String email, String gender, String height, String mobile, String name, String password, String profileImage, String status, String weight) {
        this.birthDate = birthDate;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.gender = gender;
        this.height = height;
        this.mobile = mobile;
        this.name = name;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
        this.weight = weight;
    }

    public
    SystemUsers() {
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
    String getBirthDate() {
        return birthDate;
    }

    public
    void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public
    String getBloodGroup() {
        return bloodGroup;
    }

    public
    void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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
    String getGender() {
        return gender;
    }

    public
    void setGender(String gender) {
        this.gender = gender;
    }

    public
    String getHeight() {
        return height;
    }

    public
    void setHeight(String height) {
        this.height = height;
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
    String getName() {
        return name;
    }

    public
    void setName(String name) {
        this.name = name;
    }

    public
    String getPassword() {
        return password;
    }

    public
    void setPassword(String password) {
        this.password = password;
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
    String getStatus() {
        return status;
    }

    public
    void setStatus(String status) {
        this.status = status;
    }

    public
    String getWeight() {
        return weight;
    }

    public
    void setWeight(String weight) {
        this.weight = weight;
    }
}
