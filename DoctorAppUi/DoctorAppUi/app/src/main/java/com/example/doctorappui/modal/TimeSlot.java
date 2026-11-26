package com.example.doctorappui.modal;

public
class TimeSlot {
    String time,status,timeId;

    public
    TimeSlot(String time, String status) {
        this.time = time;
        this.status = status;
    }

    public
    TimeSlot() {
    }

    public
    String getTimeId() {
        return timeId;
    }

    public
    void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public
    String getTime() {
        return time;
    }

    public
    void setTime(String time) {
        this.time = time;
    }

    public
    String getStatus() {
        return status;
    }

    public
    void setStatus(String status) {
        this.status = status;
    }
}
