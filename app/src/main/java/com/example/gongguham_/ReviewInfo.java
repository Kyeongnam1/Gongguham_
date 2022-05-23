
package com.example.gongguham_;

public class ReviewInfo {
    private int ReviewTotalScore;
    private int ReviewAvScore;
    private int ReviewNumber;

    ReviewInfo(int ReviewTotalScore, int ReviewAvScore, int ReviewNumber) {
        this.ReviewTotalScore = ReviewTotalScore;
        this.ReviewAvScore = ReviewAvScore;
        this.ReviewNumber = ReviewNumber;
    }

    public int getReviewNumber() {
        return ReviewNumber;
    }

    public void setReviewNumber(int reviewNumber) {
        ReviewNumber = reviewNumber;
    }

    public int getReviewAvScore() {
        return ReviewAvScore;
    }

    public void setReviewAvScore(int reviewAvScore) {
        ReviewAvScore = reviewAvScore;
    }

    public int getReviewTotalScore() {
        return ReviewTotalScore;
    }

    public void setReviewTotalScore(int reviewTotalScore) {
        ReviewTotalScore = reviewTotalScore;
    }
}
