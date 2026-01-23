package model;

import java.time.LocalDateTime;
import enums.PaymentMode;
import enums.PaymentStatus;

public class Payment {

    private int _paymentId;
    private PaymentMode _paymentMode;
    private PaymentStatus _paymentStatus;
    private LocalDateTime _paymentTime;
    private int _bookingId;

    // No-argument constructor
    public Payment() {}

    // Parameterized constructor
    public Payment(PaymentMode paymentMode, PaymentStatus paymentStatus, int bookingId) {
        this._paymentMode = paymentMode;
        this._paymentStatus = paymentStatus;
        this._bookingId = bookingId;
        this._paymentTime = LocalDateTime.now();
    }

    // Getters
    public int getPaymentId() {
        return _paymentId;
    }

    public PaymentMode getPaymentMode() {
        return _paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this._paymentMode = paymentMode;
    }

    public PaymentStatus getPaymentStatus() {
        return _paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this._paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentTime() {
        return _paymentTime;
    }

    public int getBookingId() {
        return _bookingId;
    }

    public void setBookingId(int bookingId) {
        this._bookingId = bookingId;
    }
    public String toString() {
        return "Payment{" +
                "_paymentId=" + _paymentId +
                ", _paymentMode=" + _paymentMode +
                ", _paymentStatus=" + _paymentStatus +
                ", _paymentTime=" + _paymentTime +
                ", _bookingId=" + _bookingId +
                '}';
    }
}
