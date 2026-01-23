package model;

import enums.BookingStatus;
import java.time.LocalDateTime;

public class Booking {

    private int _bookingId;  // auto-generated, no public setter
    private final LocalDateTime _bookingTime;  // set at creation, immutable
    private double _totalAmount;
    private BookingStatus _bookingStatus;
    private int _customerId;
    private int _showId;

    // No-arg constructor
    public Booking() {
        this._bookingTime = LocalDateTime.now();
    }

    // Parameterized constructor for creating a new booking
    public Booking(double totalAmount, BookingStatus bookingStatus, int customerId, int showId) {
        this._totalAmount = totalAmount;
        this._bookingStatus = bookingStatus;
        this._customerId = customerId;
        this._showId = showId;
        this._bookingTime = LocalDateTime.now();
    }

    // Getters
    public int getBookingId() {
        return _bookingId;
    }

    public LocalDateTime getBookingTime() {
        return _bookingTime;
    }

    public double getTotalAmount() {
        return _totalAmount;
    }

    public BookingStatus getBookingStatus() {
        return _bookingStatus;
    }

    public int getCustomerId() {
        return _customerId;
    }

    public int getShowId() {
        return _showId;
    }

    // Setters for fields that can change
    public void setTotalAmount(double totalAmount) {
        this._totalAmount = totalAmount;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this._bookingStatus = bookingStatus;
    }

    public void setCustomerId(int customerId) {
        this._customerId = customerId;
    }

    public void setShowId(int showId) {
        this._showId = showId;
    }

    // Package-private setters for DAO usage only
    void setBookingId(int bookingId) {
        this._bookingId = bookingId;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "_bookingId=" + _bookingId +
                ", _bookingTime=" + _bookingTime +
                ", _totalAmount=" + _totalAmount +
                ", _bookingStatus=" + _bookingStatus +
                ", _customerId=" + _customerId +
                ", _showId=" + _showId +
                '}';
    }
}
