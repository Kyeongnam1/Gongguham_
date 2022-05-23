
package com.example.gongguham_;

public class ReviewInfo {
    private String email;
    private int score;

    ReviewInfo(String email, int score) {
        this.email = email;
        this.score = score;
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
