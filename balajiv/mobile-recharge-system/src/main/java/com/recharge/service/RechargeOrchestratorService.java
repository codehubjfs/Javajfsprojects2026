package com.recharge.service;

import com.recharge.dao.RechargeTransactionDAO;
import com.recharge.model.RechargeTransaction;

public class RechargeOrchestratorService {
	
	private final RechargeTransactionDAO rechargeDAO = new RechargeTransactionDAO();
    private final PaymentService paymentService = new PaymentService();
    private final PostRechargeService postRechargeService = new PostRechargeService();
    
    public boolean performRecharge(int userId, int connectionId, int planId, double amount) {
    	
    	//create recharge transaction
    	RechargeTransaction tx = new RechargeTransaction(userId, connectionId, planId, amount);
    	int rechargeId = rechargeDAO.createInitiatedTransaction(tx);
    	
    	//attempt payment(maximum 2 tries only)
    	for(int attempt = 1; attempt <= 2; attempt++) {
    		boolean success = paymentService.attemptPayment(rechargeId, amount);
    		
    		if(success) {
    			// post success actions
    			postRechargeService.handleSuccessfulRecharge(rechargeId, userId);
    			return true;
    		}
    	}
    	
    	//attempts limit exceeeded then recharge failed
    	return false;
    	
    }

}
