package com.dao;

import com.model.UPIPayment;

public interface UPIPaymentDAO {

	boolean save(UPIPayment payment) throws Exception;

    UPIPayment findByPaymentId(int paymentId) throws Exception;
}
