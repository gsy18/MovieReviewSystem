import models.*;
import Exception.CustomException;

import java.util.*;

public class Main {

    static HashMap<String, HashSet<String>>usertoReviewedMoviesMap;
    static HashMap<String, HashMap<String,PriorityQueue<Movie>>>reviewMap;
    static HashMap<String, Movie>movienameToObjectMap;
    static HashMap<String, ConsolidatedMovieReview>yearToAverageScore;
    static HashMap<String, ConsolidatedMovieReview>genreToAverageScore;

    public static void main(String[] args) throws CustomException {

        reviewMap=new HashMap<>();
        usertoReviewedMoviesMap=new HashMap<>();
        movienameToObjectMap=new HashMap<>();
        yearToAverageScore=new HashMap<>();
        genreToAverageScore=new HashMap<>();

        reviewMap.put("moviesByGenreViewer",new HashMap<>());
        reviewMap.put("moviesByYearViewer",new HashMap<>());
        reviewMap.put("moviesByGenreCritics",new HashMap<>());
        reviewMap.put("moviesByYearCritics",new HashMap<>());


        addMovie("Don",Arrays.asList("Action","Comedy"),"2006");
        addMovie("Tiger",Arrays.asList("Drama"),"2008");
        addMovie("Padmavaat",Arrays.asList("Comedy"),"2006");
        addMovie("Lunchbox",Arrays.asList("Drama"),"2021");
        addMovie("Guru",Arrays.asList("Drama"),"2006");
        addMovie("Metro",Arrays.asList("Romance"),"2006");

        addUser("SRK");
        addUser("Salman");
        addUser("Deepika");

        addReview("SRK", "Don", 2);
        addReview("SRK", "Padmavaat", 8);
        addReview("Salman", "Don", 5);
        addReview("Deepika", "Don", 9);

        addReview("Deepika", "Guru", 6);
//        addReview("SRK","Don", 10) - Exception multiple reviews not
//                allowed
        addReview("Deepika", "Lunchbox", 5);
        addReview("SRK", "Tiger", 5);
        addReview("SRK", "Metro", 7);


        printByYear(1,0,"2006");
        printByYear(1,1,"2006");
        printByGenre(1,0,"Drama");
        System.out.println(yearToAverageScore.get("2006").getAverageReviewScore());

    }
    static void addUser(String name){
        usertoReviewedMoviesMap.put(name,new HashSet<>());
    }

    static void addMovie(String moviename,List<String>generesList,String year){
        Movie movie=new Movie(moviename,generesList,year);
        movienameToObjectMap.put(moviename,movie);


        reviewMap.get("moviesByYearViewer").putIfAbsent(year,new PriorityQueue<>((m1,m2)->Integer.compare(m2.viewerRating,m1.viewerRating)));
        reviewMap.get("moviesByYearCritics").putIfAbsent(year,new PriorityQueue<>((m1,m2)->Integer.compare(m2.criticsRating,m1.criticsRating)));

        yearToAverageScore.putIfAbsent(year,new ConsolidatedMovieReview());

        reviewMap.get("moviesByYearViewer").get(year).add(movie);
        reviewMap.get("moviesByYearCritics").get(year).add(movie);

        for(String genre:generesList)
        {

            genreToAverageScore.putIfAbsent(genre,new ConsolidatedMovieReview());
            reviewMap.get("moviesByGenreViewer").putIfAbsent(genre,new PriorityQueue<>((m1,m2)->Integer.compare(m2.viewerRating,m1.viewerRating)));
            reviewMap.get("moviesByGenreCritics").putIfAbsent(genre,new PriorityQueue<>((m1,m2)->Integer.compare(m2.criticsRating,m1.criticsRating)));

            reviewMap.get("moviesByGenreViewer").get(genre).add(movie);
            reviewMap.get("moviesByGenreCritics").get(genre).add(movie);
        }
    }


    static void printByGenre(int n,int choice,String genre){
        Stack<Movie>stack=new Stack<>();
        switch (choice)
        {
            case 0:
                for(int i=0;i<n;i++){
                    Movie movie=stack.push(reviewMap.get("moviesByGenreViewer").get(genre).remove());
                   System.out.println(movie.name+"-"+movie.viewerRating);
                }
                while(!stack.isEmpty())
                {
                    reviewMap.get("moviesByGenreViewer").get(genre).add(stack.pop());
                }
                break;
            case 1:
                for(int i=0;i<n;i++){
                    Movie movie=stack.push(reviewMap.get("moviesByGenreCritics").get(genre).remove());
                    System.out.println(movie.name+"-"+movie.criticsRating);
                }
                while(!stack.isEmpty())
                {
                    reviewMap.get("moviesByGenreCritics").get(genre).add(stack.pop());
                }
                break;

        }
    }


    static void printByYear(int n,int choice, String year){
        Stack<Movie>stack=new Stack<>();
        switch (choice)
        {
            case 0:
                for(int i=0;i<n;i++){
                    Movie movie=stack.push(reviewMap.get("moviesByYearViewer").get(year).remove());
                    System.out.println(movie.name+"-"+movie.viewerRating);
                }
                while(!stack.isEmpty())
                {
                    reviewMap.get("moviesByYearViewer").get(year).add(stack.pop());
                }
                break;
            case 1:
                for(int i=0;i<n;i++){
                    Movie movie=stack.push(reviewMap.get("moviesByYearCritics").get(year).remove());
                    System.out.println(movie.name+"-"+movie.criticsRating);
                }
                while(!stack.isEmpty())
                {
                    reviewMap.get("moviesByYearCritics").get(year).add(stack.pop());
                }
                break;

        }
    }

    static Double getAverageRatingByYear(String year){
        return yearToAverageScore.get(year).getAverageReviewScore();
    }

    static Double getAverageRatingByGenre(String genre){
        return genreToAverageScore.get(genre).getAverageReviewScore();
    }


    static Double getAverageRatingByMovie(String moviename){
        return movienameToObjectMap.get(moviename).getAverageReviewScore();
    }

    static void addReview(String username,String moviename,int rating) throws CustomException {
        if(!movienameToObjectMap.keySet().contains(moviename))
            throw  new CustomException("movie "+moviename+" is not present in the system");
        else if(usertoReviewedMoviesMap.get(username).contains(moviename)){
            throw  new CustomException("movie already reviewed by the user");
        }
        else if(rating<1|| rating>10){
            throw  new CustomException("Given rating not allowed");
        }
        int userRatingCounts=usertoReviewedMoviesMap.get(username).size();
        Movie movie=movienameToObjectMap.get(moviename);

        ConsolidatedMovieReview consolidatedMovieReviewForYear=yearToAverageScore.get(movie.year);

        consolidatedMovieReviewForYear.reviewCount++;
        if(userRatingCounts>=3){
            movie.criticsRating+=2*rating;
            consolidatedMovieReviewForYear.reviewSum+=2*rating;
        }
        else{
            movie.viewerRating+=rating;
            consolidatedMovieReviewForYear.reviewSum+=rating;
        }


        reviewMap.get("moviesByYearViewer").get(movie.year).remove(movie);

        reviewMap.get("moviesByYearCritics").get(movie.year).remove(movie);

        reviewMap.get("moviesByYearViewer").get(movie.year).add(movie);

        reviewMap.get("moviesByYearCritics").get(movie.year).add(movie);

        for(String genre: movie.genreList){


            ConsolidatedMovieReview consolidatedMovieReviewForGenre=genreToAverageScore.get(genre);
            consolidatedMovieReviewForGenre.reviewCount++;
            if(userRatingCounts>=3){
                consolidatedMovieReviewForGenre.reviewSum+=2*rating;
            }
            else{
                consolidatedMovieReviewForGenre.reviewSum+=rating;
            }
            reviewMap.get("moviesByGenreViewer").get(genre).remove(movie);
            reviewMap.get("moviesByGenreCritics").get(genre).remove(movie);
            reviewMap.get("moviesByGenreViewer").get(genre).add(movie);
            reviewMap.get("moviesByGenreCritics").get(genre).add(movie);
        }

        usertoReviewedMoviesMap.get(username).add(moviename);
        movie.ratingCount++;
    }
}
