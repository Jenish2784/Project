package com.example.dotechuser.Model;

public class Following {
    String followingBy;
    long followingAt;

    public Following() {
    }

    public Following(String followingBy, long followingAt) {
        this.followingBy = followingBy;
        this.followingAt = followingAt;
    }

    public String getFollowingBy() {
        return followingBy;
    }

    public void setFollowingBy(String followingBy) {
        this.followingBy = followingBy;
    }

    public long getFollowingAt() {
        return followingAt;
    }

    public void setFollowingAt(long followingAt) {
        this.followingAt = followingAt;
    }
}
