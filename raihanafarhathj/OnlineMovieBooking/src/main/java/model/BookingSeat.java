package model;

public class BookingSeat {

    private int _bookingSeatId; 
    private int _bookingId;
    private int _showSeatId;

    // No-argument constructor
    public BookingSeat() {}

    // Parameterized constructor
    public BookingSeat(int bookingId, int showSeatId) {
        this._bookingId = bookingId;
        this._showSeatId = showSeatId;
    }

    // Getters
    public int getBookingSeatId() {
        return _bookingSeatId;
    }

    public int getBookingId() {
        return _bookingId;
    }

    public void setBookingId(int bookingId) {
        this._bookingId = bookingId;
    }

    public int getShowSeatId() {
        return _showSeatId;
    }

    public void setShowSeatId(int showSeatId) {
        this._showSeatId = showSeatId;
    }

    public String toString() {
        return "BookingSeat{" +
                "_bookingSeatId=" + _bookingSeatId +
                ", _bookingId=" + _bookingId +
                ", _showSeatId=" + _showSeatId +
                '}';
    }
}
