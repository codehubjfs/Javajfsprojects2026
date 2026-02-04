package com.recharge.service;

import com.recharge.dao.PaymentDAO;
import com.recharge.dao.RechargeTransactionDAO;


public class UserRefundRequestService {

	private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RechargeTransactionDAO rechargeDAO = new RechargeTransactionDAO();

    /**
     * used to request refund 
     * @param transactionRef
     */
    public void requestRefund(String transactionRef) {

    	int rechargeId = paymentDAO.getRechargeIdByTransactionRef(transactionRef);

        String rechargeStatus = rechargeDAO.getStatus(rechargeId);

        String paymentStatus = paymentDAO.getPaymentStatusByTxnRef(transactionRef);

        if (!"FAILED".equalsIgnoreCase(rechargeStatus)) {
            throw new RuntimeException("Refund request rejected: recharge not failed");
        }

        if (!"SUCCESS".equalsIgnoreCase(paymentStatus)) {
            throw new RuntimeException("Refund request rejected: payment not successful");
        }

        System.out.println("""
            
            Refund request submitted successfully.
            Please contact admin with your Transaction Reference.
            Transaction Reference: %s
            """.formatted(transactionRef));
    }
}
