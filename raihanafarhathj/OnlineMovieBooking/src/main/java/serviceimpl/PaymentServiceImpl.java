package serviceimpl;

import service.PaymentService;
import dao.PaymentDAO;
import dao.impl.PaymentDAOImpl;
import model.Payment;
import exception.DataAccessException;
import exception.NotFoundException;
import exception.ServiceException;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    @Override
    public int addPayment(Payment payment) throws ServiceException {
        try {
            if (payment == null) {
                throw new ServiceException("Cannot add null payment.");
            }
            return paymentDAO.addPayment(payment);
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to add payment: " + ex.getMessage());
        }
    }

    @Override
    public Payment getPaymentByBooking(int bookingId) throws NotFoundException, ServiceException {
        try {
            Payment payment = paymentDAO.getPaymentByBooking(bookingId);
            if (payment == null) {
                throw new NotFoundException("Payment for booking ID " + bookingId + " not found.");
            }
            return payment;
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to fetch payment: " + ex.getMessage());
        }
    }
}
