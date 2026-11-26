package com.example.dotechuser.Model;

public class Doctor {

    private String profileImage;
    private String name;
    private String category;
    private String msgAmount;
    private String callAmount;
    private String uId;
    private String type;
    private String emailId;
    private String experience;
    private String address;
    private String timeSlote;
    private String totalAmount;
    private String mobileNumber;
    private Float rating;

    public Doctor() {
    }

    public Doctor(String profileImage, String name, String category, String msgAmount, String callAmount, String experience,String address,String totalAmount) {
        this.profileImage = profileImage;
        this.name = name;
        this.category = category;
        this.msgAmount = msgAmount;
        this.callAmount = callAmount;
        this.experience = experience;
        this.address = address;
        this.totalAmount = totalAmount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTimeSlote() {
        return timeSlote;
    }

    public void setTimeSlote(String timeSlote) {
        this.timeSlote = timeSlote;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(String callAmount) {
        this.callAmount = callAmount;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMsgAmount() {
        return msgAmount;
    }

    public void setMsgAmount(String msgAmount) {
        this.msgAmount = msgAmount;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
