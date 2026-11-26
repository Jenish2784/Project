package com.example.dotechuser.Model;

public class Consultation {
    private String userId, doctorId, doctorProfileImage, type, category, profileImage, userName, userEmail, mobileNumber,
            doctorName, gender, birthDate, height, weight, bloodGroup, date, time, problem, status, totalAmount,
            paymentId, paymentStatus, consultId, problemImage;
    private String address, meetLink, currentDate, currentTime;

    public Consultation() {
    }

    public Consultation(String userId, String doctorId, String doctorProfileImage, String type, String category,
                        String profileImage, String userName, String userEmail, String mobileNumber, String doctorName,
                        String gender, String birthDate, String height, String weight, String bloodGroup, String date,
                        String time, String problem, String status, String totalAmount, String paymentId,
                        String paymentStatus, String address, String meetLink, String currentDate, String currentTime, String problemImage) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorProfileImage = doctorProfileImage;
        this.type = type;
        this.category = category;
        this.profileImage = profileImage;
        this.userName = userName;
        this.userEmail = userEmail;
        this.mobileNumber = mobileNumber;
        this.doctorName = doctorName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
        this.date = date;
        this.time = time;
        this.problem = problem;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.address = address;
        this.meetLink = meetLink;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.problemImage = problemImage;
    }

    public String getProblemImage() {
        return problemImage;
    }

    public void setProblemImage(String problemImage) {
        this.problemImage = problemImage;
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

    public String getConsultId() {
        return consultId;
    }

    public void setConsultId(String consultId) {
        this.consultId = consultId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorProfileImage() {
        return doctorProfileImage;
    }

    public void setDoctorProfileImage(String doctorProfileImage) {
        this.doctorProfileImage = doctorProfileImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeetLink() {
        return meetLink;
    }

    public void setMeetLink(String meetLink) {
        this.meetLink = meetLink;
    }
}
