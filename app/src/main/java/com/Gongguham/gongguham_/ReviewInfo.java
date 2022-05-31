
package com.Gongguham.gongguham_;

public class ReviewInfo {
    private String email;
    private int score;
    private String comment;

    ReviewInfo(String email, int score, String comment) {
        this.email = email;
        this.score = score;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
