package com.dao;

import com.model.NetBankingPayment;

public interface NetBankingPaymentDAO {
	
	boolean save(NetBankingPayment payment) throws Exception;

    NetBankingPayment findByPaymentId(int paymentId) throws Exception;
}
