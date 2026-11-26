package com.example.doctorappui.modal;

public
class Department {
    private String departmentIcon;
    private String departmentName;
    private String userId;
    private String fees;
    private long timeAt;

    public
    Department() {
    }


    public
    String getFees() {
        return fees;
    }

    public
    void setFees(String fees) {
        this.fees = fees;
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
    long getTimeAt() {
        return timeAt;
    }

    public
    void setTimeAt(long timeAt) {
        this.timeAt = timeAt;
    }

    public
    String getDepartmentIcon() {
        return departmentIcon;
    }

    public
    void setDepartmentIcon(String departmentIcon) {
        this.departmentIcon = departmentIcon;
    }

    public
    String getDepartmentName() {
        return departmentName;
    }

    public
    void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
