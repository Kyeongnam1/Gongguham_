
package com.Gongguham.gongguham_;

public class ReviewToUserInfo {
    private double ReviewTotalScore;
    private double ReviewAvScore;
    private double ReviewNumber;


    ReviewToUserInfo(double ReviewTotalScore, double ReviewAvScore, double ReviewNumber) {
        this.ReviewTotalScore = ReviewTotalScore;
        this.ReviewAvScore = ReviewAvScore;
        this.ReviewNumber = ReviewNumber;

    }



    public double getReviewNumber() {
        return ReviewNumber;
    }

    public void setReviewNumber(double reviewNumber) {
        ReviewNumber = reviewNumber;
    }

    public double getReviewAvScore() {
        return ReviewAvScore;
    }

    public void setReviewAvScore(double reviewAvScore) {
        ReviewAvScore = reviewAvScore;
    }

    public double getReviewTotalScore() {
        return ReviewTotalScore;
    }

    public void setReviewTotalScore(double reviewTotalScore) {
        ReviewTotalScore = reviewTotalScore;
    }
}
