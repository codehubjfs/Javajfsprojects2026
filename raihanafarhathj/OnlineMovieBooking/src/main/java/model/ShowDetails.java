package model;

import java.time.LocalDate;
import java.time.LocalTime;

import enums.ShowStatus;

public class ShowDetails {

    private int _showId;
    private LocalDate _showDate;
    private LocalTime _startTime;
    private LocalTime _endTime;
    private double _ticketPrice;
    private ShowStatus _showStatus;
    private int _movieId;
    private int _hallId;

    // No-argument constructor
    public ShowDetails() {}

    // Parameterized constructor
    public ShowDetails(LocalDate showDate, LocalTime startTime, LocalTime endTime,
                       double ticketPrice, ShowStatus showStatus, int movieId, int hallId) {
        this._showDate = showDate;
        this._startTime = startTime;
        this._endTime = endTime;
        this._ticketPrice = ticketPrice;
        this._showStatus = showStatus;
        this._movieId = movieId;
        this._hallId = hallId;
    }

    // Getters and Setters
    public int getShowId() {
        return _showId;
    }

    public LocalDate getShowDate() {
        return _showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this._showDate = showDate;
    }

    public LocalTime getStartTime() {
        return _startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this._startTime = startTime;
    }

    public LocalTime getEndTime() {
        return _endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this._endTime = endTime;
    }

    public double getTicketPrice() {
        return _ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this._ticketPrice = ticketPrice;
    }

    public ShowStatus getShowStatus() {
        return _showStatus;
    }

    public void setShowStatus(ShowStatus showStatus) {
        this._showStatus = showStatus;
    }

    public int getMovieId() {
        return _movieId;
    }

    public void setMovieId(int movieId) {
        this._movieId = movieId;
    }

    public int getHallId() {
        return _hallId;
    }

    public void setHallId(int hallId) {
        this._hallId = hallId;
    }
    
    public String toString() {
        return "ShowDetails{" +
                "_showId=" + _showId +
                ", _showDate=" + _showDate +
                ", _startTime=" + _startTime +
                ", _endTime=" + _endTime +
                ", _ticketPrice=" + _ticketPrice +
                ", _showStatus=" + _showStatus +
                ", _movieId=" + _movieId +
                ", _hallId=" + _hallId +
                '}';
    }
}
