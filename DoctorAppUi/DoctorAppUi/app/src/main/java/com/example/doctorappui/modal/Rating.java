package com.example.doctorappui.modal;

public
class Rating {
    double rating;
    String review;
    long timedAt;
    String ratingBy;

    public
    Rating() {
    }

    public
    double getRating() {
        return rating;
    }

    public
    void setRating(double rating) {
        this.rating = rating;
    }

    public
    String getReview() {
        return review;
    }

    public
    void setReview(String review) {
        this.review = review;
    }

    public
    long getTimedAt() {
        return timedAt;
    }

    public
    void setTimedAt(long timedAt) {
        this.timedAt = timedAt;
    }

    public
    String getRatingBy() {
        return ratingBy;
    }

    public
    void setRatingBy(String ratingBy) {
        this.ratingBy = ratingBy;
    }
}
