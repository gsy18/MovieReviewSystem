package models;

public class ConsolidatedMovieReview {
    public int reviewSum;
    public int reviewCount;
    public ConsolidatedMovieReview(){

    }
    public double getAverageReviewScore(){
        return Math.round((reviewSum/(1D*reviewCount))*100)/100D;
    }
}
