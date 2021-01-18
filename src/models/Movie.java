package models;

import java.util.List;

public class Movie {
    public String name;
    public int viewerRating;
    public int criticsRating;
    public List<String> genreList;
    public int ratingCount;
    public String year;
    public Movie(String name,List<String> genreList,String year){
        this.name=name;
        viewerRating=0;
        criticsRating=0;
        this.genreList=genreList;
        this.ratingCount=0;
        this.year=year;
    }
    public double getAverageReviewScore(){
        return Math.round((viewerRating+criticsRating)*100)/100D;
    }
}
