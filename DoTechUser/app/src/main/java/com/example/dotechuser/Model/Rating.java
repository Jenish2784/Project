package com.example.dotechuser.Model;

public class Rating {

    String ratingBy,doctorId,currentDate,message;
    Long timeAt;
    Float rating;



    public Rating() {
    }

    public Rating(String message,Float rating,String ratingBy,Long timeAt){
        this.message = message;
        this.rating = rating;
        this.ratingBy = ratingBy;
        this.timeAt = timeAt;
    }

    public Rating(String ratingBy, String doctorId, String currentDate, String message, Long timeAt, Float rating) {
        this.ratingBy = ratingBy;
        this.doctorId = doctorId;
        this.currentDate = currentDate;
        this.message = message;
        this.timeAt = timeAt;
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRatingBy() {
        return ratingBy;
    }

    public void setRatingBy(String ratingBy) {
        this.ratingBy = ratingBy;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Long getTimeAt() {
        return timeAt;
    }

    public void setTimeAt(Long timeAt) {
        this.timeAt = timeAt;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
