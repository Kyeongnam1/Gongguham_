package com.example.gongguham_;

public class UserReviewInfo {
    private String comment;
    private String email;
    private int score;

    public UserReviewInfo(String comment, String email, int score) {
        this.comment = comment;
        this.email = email;
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
