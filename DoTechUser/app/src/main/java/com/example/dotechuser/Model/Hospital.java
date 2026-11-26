package com.example.dotechuser.Model;

public class Hospital {
    private String HospitalId;
    private String profileImage;
    private String name;
    private String category;
    private String emailId;
    private String type;
    private String status;
    private String totalAmount;
    private String msgAmount;
    private String callAmount;
    private String address;
    private String departmentIcon;
    private String departmentName;
    private String mobileNumber;
    private String fees;

    public Hospital() {
    }

    public Hospital(String HospitalId, String name, String type) {
        this.HospitalId = HospitalId;
        this.name = name;
        this.type = type;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(String callAmount) {
        this.callAmount = callAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDepartmentIcon() {
        return departmentIcon;
    }

    public void setDepartmentIcon(String departmentIcon) {
        this.departmentIcon = departmentIcon;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getHospitalId() {
        return HospitalId;
    }

    public void setHospitalId(String hospitalId) {
        HospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
