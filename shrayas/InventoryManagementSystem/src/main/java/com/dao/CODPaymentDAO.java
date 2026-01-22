package com.dao;

import com.enums.DeliveryStatus;
import com.model.CODPayment;

public interface CODPaymentDAO {
	
	boolean save(CODPayment payment) throws Exception;

    CODPayment findByPaymentId(int paymentId) throws Exception;

    boolean updateDeliveryStatus(int paymentId, DeliveryStatus status) throws Exception;
}
