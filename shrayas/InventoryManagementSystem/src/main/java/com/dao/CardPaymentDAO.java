package com.dao;

import com.model.CardPayment;

public interface CardPaymentDAO {

	boolean save(CardPayment cardPayment) throws Exception;

    CardPayment findByPaymentId(int paymentId) throws Exception;
}
