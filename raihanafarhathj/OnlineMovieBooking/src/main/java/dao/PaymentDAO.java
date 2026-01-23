package dao;

import model.Payment;
import exception.DataAccessException;

public interface PaymentDAO {
    int addPayment(Payment payment) throws DataAccessException;
    Payment getPaymentByBooking(int bookingId) throws DataAccessException;
}
