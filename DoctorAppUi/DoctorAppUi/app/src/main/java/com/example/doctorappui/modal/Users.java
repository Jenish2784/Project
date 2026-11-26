package com.example.doctorappui.modal;

public
class Users {
    String emailId,mobileNumber,password,name,degreeName,category,degreeImage;
    String address,accountNo,ifscNo,callDescription,msgDescription,profileImage;
    String type,status,msgAmount,callAmount,userId,totalAmount,depositAmount;

    public Users(){

    }

    public
    Users(String emailId, String mobileNumber, String password, String name, String degreeName, String category, String degreeImage, String address, String accountNo, String ifscNo, String callDescription, String msgDescription, String profileImage, String type, String status, String msgAmount, String callAmount,String totalAmount,String depositAmount) {
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.name = name;
        this.degreeName = degreeName;
        this.category = category;
        this.degreeImage = degreeImage;
        this.address = address;
        this.accountNo = accountNo;
        this.ifscNo = ifscNo;
        this.callDescription = callDescription;
        this.msgDescription = msgDescription;
        this.profileImage = profileImage;
        this.type = type;
        this.status = status;
        this.msgAmount = msgAmount;
        this.callAmount = callAmount;
        this.totalAmount = totalAmount;
        this.depositAmount = depositAmount;
    }

    public
    String getDepositAmount() {
        return depositAmount;
    }

    public
    void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public
    String getTotalAmount() {
        return totalAmount;
    }

    public
    void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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
    String getEmailId() {
        return emailId;
    }

    public
    void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public
    String getMobileNumber() {
        return mobileNumber;
    }

    public
    void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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
    String getName() {
        return name;
    }

    public
    void setName(String name) {
        this.name = name;
    }

    public
    String getDegreeName() {
        return degreeName;
    }

    public
    void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
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
    String getDegreeImage() {
        return degreeImage;
    }

    public
    void setDegreeImage(String degreeImage) {
        this.degreeImage = degreeImage;
    }

    public
    String getAddress() {
        return address;
    }

    public
    void setAddress(String address) {
        this.address = address;
    }

    public
    String getAccountNo() {
        return accountNo;
    }

    public
    void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public
    String getIfscNo() {
        return ifscNo;
    }

    public
    void setIfscNo(String ifscNo) {
        this.ifscNo = ifscNo;
    }

    public
    String getCallDescription() {
        return callDescription;
    }

    public
    void setCallDescription(String callDescription) {
        this.callDescription = callDescription;
    }

    public
    String getMsgDescription() {
        return msgDescription;
    }

    public
    void setMsgDescription(String msgDescription) {
        this.msgDescription = msgDescription;
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
    String getType() {
        return type;
    }

    public
    void setType(String type) {
        this.type = type;
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
    String getMsgAmount() {
        return msgAmount;
    }

    public
    void setMsgAmount(String msgAmount) {
        this.msgAmount = msgAmount;
    }

    public
    String getCallAmount() {
        return callAmount;
    }

    public
    void setCallAmount(String callAmount) {
        this.callAmount = callAmount;
    }
}
