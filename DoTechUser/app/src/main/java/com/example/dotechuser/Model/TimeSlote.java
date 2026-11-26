package com.example.dotechuser.Model;

public class TimeSlote {
    private String time;
    private String status;
    private String timeId;

    public TimeSlote() {
    }

    public TimeSlote(String time, String status, String timeId) {
        this.time = time;
        this.status = status;
        this.timeId = timeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }
}
