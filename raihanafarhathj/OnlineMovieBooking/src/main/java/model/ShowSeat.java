package model;

import enums.SeatStatus;

public class ShowSeat {

    private int _showSeatId; 
    private SeatStatus _seatStatus;
    private int _showId;
    private int _seatId;

    // No-argument constructor
    public ShowSeat() {}

    // Parameterized constructor
    public ShowSeat(SeatStatus seatStatus, int showId, int seatId) {
        this._seatStatus = seatStatus;
        this._showId = showId;
        this._seatId = seatId;
    }

    // Getters
    public int getShowSeatId() {
        return _showSeatId;
    }

    public SeatStatus getSeatStatus() {
        return _seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this._seatStatus = seatStatus;
    }

    public int getShowId() {
        return _showId;
    }

    public void setShowId(int showId) {
        this._showId = showId;
    }

    public int getSeatId() {
        return _seatId;
    }

    public void setSeatId(int seatId) {
        this._seatId = seatId;
    }

    public String toString() {
        return "ShowSeat{" +
                "_showSeatId=" + _showSeatId +
                ", _seatStatus=" + _seatStatus +
                ", _showId=" + _showId +
                ", _seatId=" + _seatId +
                '}';
    }
}
