package com.recharge.service;

import com.recharge.dao.AuditLogDAO;
import com.recharge.dao.ConnectionDAO;
import com.recharge.dao.PaymentDAO;
import com.recharge.dao.RechargeTransactionDAO;
import com.recharge.dao.RefundDAO;
import com.recharge.model.Payment;

public class AdminRefundService {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RechargeTransactionDAO rechargeDAO = new RechargeTransactionDAO();
    private final ConnectionDAO connectionDAO = new ConnectionDAO();
    private final RefundDAO refundDAO = new RefundDAO();
    private final AuditLogDAO auditDAO = new AuditLogDAO();

    
    /**
     * used to initiate refund on the db
     * @param transactionRef
     * @param mobileNumber
     * @param adminUserId
     */
    public void initiateRefund(String transactionRef, String mobileNumber, int adminUserId) {

        Payment payment = paymentDAO.findSuccessfulPaymentByTxnRef(transactionRef);

        String rechargeStatus = rechargeDAO.getStatus(payment.getRechargeId());

        if (!"FAILED".equalsIgnoreCase(rechargeStatus)) {
            throw new RuntimeException("Refund not allowed: recharge not failed");
        }

        int connectionId = rechargeDAO.getConnectionId(payment.getRechargeId());

        String registeredMobile = connectionDAO.getMobileNumber(connectionId);

        // check mobile number 
        if (!registeredMobile.equals(mobileNumber)) {
            throw new RuntimeException("Mobile number mismatch");
        }

        // check refund already exists
        if (refundDAO.refundExists(payment.getPaymentId())) {
            throw new RuntimeException("Refund already initiated");
        }
        
        refundDAO.createRefund(payment.getPaymentId(), payment.getAmount());

        rechargeDAO.updateStatus(payment.getRechargeId(), "REFUND_INITIATED");

        auditDAO.log(adminUserId, "REFUND", null, "INITIATE", null, transactionRef);

        System.out.println("Refund initiated successfully");
    }

   /**
    * used to complete refund on the db
    * @param transactionRef
    * @param adminUserId
    */
    
    public void completeRefund(String transactionRef, int adminUserId) {
    	
    	int refundId = refundDAO.getRefundIdByTransactionRef(transactionRef);

        refundDAO.completeRefund(refundId);
        
        int rechargeId = paymentDAO.getRechargeIdByTransactionRef(transactionRef);
        
        rechargeDAO.updateStatus(rechargeId, "REFUNDED");
        
        auditDAO.log(adminUserId, "REFUND", refundId, "COMPLETE", "INITIATED", "REFUNDED");

        System.out.println("Refund completed successfully");
    }
}
