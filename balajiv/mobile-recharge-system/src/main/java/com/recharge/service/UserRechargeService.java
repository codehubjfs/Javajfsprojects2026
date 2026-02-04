package com.recharge.service;

import com.recharge.dao.*;



public class UserRechargeService {

	private final ConnectionDAO connectionDAO = new ConnectionDAO();
    private final OperatorDAO operatorDAO = new OperatorDAO();
    private final RechargePlanDAO planDAO = new RechargePlanDAO();
    private final RechargeTransactionDAO txDAO = new RechargeTransactionDAO();
    private final PaymentService paymentService = new PaymentService();
    private final OfferApplicationService offerService = new OfferApplicationService();
    private final PostRechargeService postRechargeService = new PostRechargeService();
    private final AdminReadService adminReadService = new AdminReadService();
    
    /**
     * used to show available Plans for particular operator
     * @param mobile
     */
    public void showAvailablePlans(String mobile) {
    	int connectionId = connectionDAO.getConnectionId(mobile);
    	int operatorId = operatorDAO.getOperatorIdByConnection(connectionId);
    	
    	adminReadService.viewActivePlansByOperator(operatorId);
    }
    
    /**
     * used to perform recharge
     * @param userId
     * @param mobile
     * @param planName
     * @param paymentMethod
     */
        
    public void recharge(int userId, String mobile, String planName, String paymentMethod) {
    	
    	int connectionId = connectionDAO.getConnectionId(mobile);
        int operatorId = operatorDAO.getOperatorIdByConnection(connectionId);
        int planId = planDAO.getActivePlanId(operatorId, planName);
        
        // original plan price
        double baseAmount = planDAO.getPlanPriceById(planId);

        // after offer applied
        double finalAmount = offerService.applyOfferIfAny(planId, baseAmount);
        
        int rechargeId = txDAO.createRecharge(userId, connectionId, planId, finalAmount);
        
        int maxAttempts = 2;
        boolean success = false;
        
        
        
        // it handles retries + txn ref + final status
        for(int attempt = 1; attempt <= maxAttempts; attempt++) {
        	try {
        		success = paymentService.attemptPayment(rechargeId, finalAmount, paymentMethod);
        		
        		if(success) {
        			postRechargeService.handleSuccessfulRecharge(rechargeId, userId);
        			System.out.println("Recharge completed successfully");
        			return;
        		}
        		
        		if(attempt < maxAttempts) {
        			System.out.println("Payment failed. retry("+attempt+"/"+maxAttempts+")");
        		}
        	}
        	catch(RuntimeException e) {
        		System.out.println(e.getMessage());
        	}
        }
        
        txDAO.updateStatus(rechargeId, "FAILED");
        System.out.println("Recharge failed after "+ maxAttempts+" attempts. you can tyr again later");
    }
}
