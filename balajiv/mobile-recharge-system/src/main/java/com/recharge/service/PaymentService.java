package com.recharge.service;

import com.recharge.dao.PaymentDAO;
import com.recharge.dao.RechargeTransactionDAO;
import com.recharge.model.Payment;


public class PaymentService {

	private final PaymentDAO paymentDAO = new PaymentDAO();
	private final RechargeTransactionDAO rechargeDAO = new RechargeTransactionDAO();
	private final PaymentMode paymentMode = PaymentMode.ALWAYS_SUCCESS;
	
	public boolean attemptPayment(int rechargeId, double amount) {
		
		rechargeDAO.updateStatus(rechargeId, "PAYMENT_IN_PROGRESS");
		int attemptNumber = paymentDAO.getNextAttemptNumber(rechargeId);
		
		// simulate payment result
		boolean success;
		switch(paymentMode) {
			case ALWAYS_SUCCESS:
		        success = true;
		        break;
	
		    case ALWAYS_FAIL:
		        success = false;
		        break;
	
		    case FAIL_ONCE_THEN_SUCCESS:
		        success = attemptNumber > 1;
		        break;
	
		    default:
		        success = false;
		}
		
		String status;
		if(success) {
			status = "SUCCESS";
		}
		else {
			status = "FAILED";
		}
		
		String failureReason;
		if(success) {
			failureReason = null;
		}
		else {
			failureReason = "Simulated payment is failure";
		}
		
		// record payment attempt
		Payment payment = new Payment(rechargeId, "UPI", amount, status, attemptNumber, failureReason);
		paymentDAO.recordPayment(payment);
		
		
		//update recharge status
		if(success) {
			rechargeDAO.updateStatus(rechargeId, "SUCCESS");
			return true;
		}
		else {
			rechargeDAO.updateStatus(rechargeId, "FAILED");
			return false;
		}
	}
	
}
