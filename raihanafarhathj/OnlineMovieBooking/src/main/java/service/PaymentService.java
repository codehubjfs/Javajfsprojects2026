package service;

import model.Payment;
import exception.NotFoundException;
import exception.ServiceException;

public interface PaymentService {
    int addPayment(Payment payment) throws ServiceException;
    Payment getPaymentByBooking(int bookingId) throws NotFoundException, ServiceException;
}
