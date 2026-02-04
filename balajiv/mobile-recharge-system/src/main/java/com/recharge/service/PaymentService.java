package com.recharge.service;

import com.recharge.dao.PaymentDAO;
import com.recharge.dao.RechargeTransactionDAO;
import com.recharge.model.Payment;
import com.recharge.util.TransactionRefUtil;


public class PaymentService {

	private final PaymentDAO paymentDAO = new PaymentDAO();
	private final RechargeTransactionDAO rechargeDAO = new RechargeTransactionDAO();
	private final PaymentMode paymentMode = PaymentMode.FAIL_ONCE_THEN_SUCCESS; // always payment success
	
	/**
	 * used to attempt payment 
	 * @param rechargeId
	 * @param amount
	 * @param paymentMethod
	 * @return
	 */
	public boolean attemptPayment(int rechargeId, double amount, String paymentMethod) {
		
		int attemptNumber = paymentDAO.getNextAttemptNumber(rechargeId);
		
		// enforce retry limit
		if(attemptNumber > 2) {
			rechargeDAO.updateStatus(rechargeId, "FAILED");
			throw new RuntimeException("Maximum payment attempts exceeded");
		}
		
		rechargeDAO.updateStatus(rechargeId, "PAYMENT_IN_PROGRESS");
		
		boolean success;
		switch(paymentMode) {
			case ALWAYS_SUCCESS -> success = true;
			case ALWAYS_FAIL -> success = false;
	        case FAIL_ONCE_THEN_SUCCESS -> success = attemptNumber > 1;
	        default -> success = false;
		}
		
		String txnRef = TransactionRefUtil.generate();
		
		Payment payment = new Payment(rechargeId, paymentMethod, amount, success ? "SUCCESS" : "FAILED",
				txnRef, attemptNumber, success ? null : "Simulated failure");
		
		paymentDAO.recordPayment(payment);
		
		if(success) {
			rechargeDAO.updateStatus(rechargeId, "SUCCESS");
			return true;
		}
		
		//attempt failed
		if(attemptNumber == 2) {
			rechargeDAO.updateStatus(rechargeId, "FAILED");
		}
		
		return false;
	}	
}
