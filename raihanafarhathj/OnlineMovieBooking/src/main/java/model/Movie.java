package model;

import enums.AgeRating;
import enums.MovieStatus;

public class Movie {

    private int _movieId;
    private String _movieName;
    private String _language;
    private String _genre;
    private int _duration; 
    private double _rating; 
    private AgeRating _ageRating;
    private MovieStatus _status;

    // No-argument constructor
    public Movie() {}

    // Parameterized constructor
    public Movie(String movieName, String language, String genre, int duration,
                 double rating, AgeRating ageRating, MovieStatus status) {
        this._movieName = movieName;
        this._language = language;
        this._genre = genre;
        this._duration = duration;
        this._rating = rating;
        this._ageRating = ageRating;
        this._status = status;
    }

    // Getters and Setters
    public int getMovieId() {
        return _movieId;
    }

    public String getMovieName() {
        return _movieName;
    }

    public void setMovieName(String movieName) {
        this._movieName = movieName;
    }

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String language) {
        this._language = language;
    }

    public String getGenre() {
        return _genre;
    }

    public void setGenre(String genre) {
        this._genre = genre;
    }

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int duration) {
        this._duration = duration;
    }

    public double getRating() {
        return _rating;
    }

    public void setRating(double rating) {
        this._rating = rating;
    }

    public AgeRating getAgeRating() {
        return _ageRating;
    }

    public void setAgeRating(AgeRating ageRating) {
        this._ageRating = ageRating;
    }

    public MovieStatus getStatus() {
        return _status;
    }

    public void setStatus(MovieStatus status) {
        this._status = status;
    }
    
    public String toString() {
        return "Movie{" +
                "_movieId=" + _movieId +
                ", _movieName='" + _movieName + '\'' +
                ", _language='" + _language + '\'' +
                ", _genre='" + _genre + '\'' +
                ", _duration=" + _duration +
                ", _rating=" + _rating +
                ", _ageRating=" + _ageRating +
                ", _status=" + _status +
                '}';
    }
}
