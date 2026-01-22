package com.dao;

import com.model.Payment;

public interface PaymentDAO {

	int createPayment(Payment payment) throws Exception;

    Payment findById(int paymentId) throws Exception;

    Payment findByTransactionId(String transactionId) throws Exception;
}
