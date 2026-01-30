package com.vserv.service;

import java.sql.SQLException;
import java.util.List;

import com.vserv.dao.impl.*;
import com.vserv.dao.interfaces.*;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;

public class FeedbackService {
    private FeedbackDAO feedbackDAO;
    private RecordDAO recordDAO;
    private InvoiceDAO invoiceDAO;

    public FeedbackService() {
        this.feedbackDAO = new FeedbackDAOImpl();
        this.recordDAO = new RecordDAOImpl();
        this.invoiceDAO = new InvoiceDAOImpl();
    }

    public Feedback submitFeedback(int serviceId, int customerId, 
            int rating, String feedbackText) throws BusinessLogicException {
        try {
            // Check if feedback already exists
            Feedback existing = feedbackDAO.findByServiceId(serviceId);
            if (existing != null) {
                throw new BusinessLogicException("Feedback already submitted for this service");
            }

            // Verify service is completed
            ServiceRecord record = recordDAO.findById(serviceId);
            if (record == null) {
                throw new BusinessLogicException("Service record not found");
            }
            if (!record.getStatus().equals("COMPLETED")) {
                throw new BusinessLogicException("Can only provide feedback for completed services");
            }

            // Verify payment is done
            Invoice invoice = invoiceDAO.findByServiceId(serviceId);
            if (invoice == null || !invoice.getPaymentStatus().equals("PAID")) {
                throw new BusinessLogicException("Feedback can only be submitted after payment is completed");
            }

            if (rating < 1 || rating > 5) {
                throw new BusinessLogicException("Rating must be between 1 and 5");
            }

            Feedback feedback = new Feedback();
            feedback.setServiceId(serviceId);
            feedback.setCustomerId(customerId);
            feedback.setRating(rating);
            feedback.setFeedbackText(feedbackText);

            return feedbackDAO.insert(feedback);

        } catch (SQLException e) {
            throw new BusinessLogicException("Error submitting feedback: " + e.getMessage());
        }
    }

    public List<Feedback> getCustomerFeedback(int customerId) throws BusinessLogicException {
        try {
            return feedbackDAO.findByCustomerId(customerId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching feedback: " + e.getMessage());
        }
    }

    public Feedback getServiceFeedback(int serviceId) throws BusinessLogicException {
        try {
            return feedbackDAO.findByServiceId(serviceId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching feedback: " + e.getMessage());
        }
    }
}