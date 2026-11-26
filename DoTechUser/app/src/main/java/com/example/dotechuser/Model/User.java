package com.example.dotechuser.Model;

public class User {
    private String name;
    private String mobile;
    private String email;
    private String password;
    private String profileImage;
    private String status;
    private String gender;
    private String bloodGroup;
    private String birthDate;
    private String height;
    private String weight;
    private String userId;


    public User(){

    }

    public User(String email, String name, String mobile, String password, String status, String profileImage, String gender, String bloodGroup, String birthDate, String height, String weight) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.password = password;
        this.status = status;
        this.profileImage = profileImage;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
