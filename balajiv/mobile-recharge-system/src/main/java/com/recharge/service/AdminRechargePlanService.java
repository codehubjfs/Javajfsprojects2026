package com.recharge.service;

import com.recharge.dao.AuditLogDAO;
import com.recharge.dao.OperatorDAO;
import com.recharge.dao.PlanUpdationHistoryDAO;
import com.recharge.dao.RechargePlanDAO;
import com.recharge.model.RechargePlan;

public class AdminRechargePlanService {

	private final RechargePlanDAO planDAO = new RechargePlanDAO();
	private final PlanUpdationHistoryDAO historyDAO = new PlanUpdationHistoryDAO();
	private final AuditLogDAO auditDAO = new AuditLogDAO();
	private final OperatorDAO operator = new OperatorDAO();

	
	// used to create the recharge plan
	public void createPlan(RechargePlan plan, int adminUserId) {
		
		if(operator.getOperatorStatus(plan.getOperatorId()).equalsIgnoreCase("ACTIVE")) {	
			
			int planId = planDAO.createPlan(plan);
			
			auditDAO.log(adminUserId, "RECHARGE_PLAN", planId, "CREATE", null, plan.getPlanName());
			
			System.out.println("Recharge plan created successfully");
		}
		else {
			System.out.println("You can't create the recharge plan for INACTIVE Operators");
		}
    }
	
	// create recharge plan using operator name
	public void createPlanByOperatorName(String operatorName, RechargePlan planInput, int adminUserId) {

	    int operatorId = operator.getOperatorIdByName(operatorName);

	    String operatorStatus = operator.getOperatorStatus(operatorId);
	    if (!operatorStatus.equalsIgnoreCase("ACTIVE")) {
	        throw new RuntimeException("You can't create the recharge plan for INACTIVE operators");
	    }

	    // Create a NEW valid domain object
	    RechargePlan plan = new RechargePlan(operatorId, planInput.getPlanName(), planInput.getPrice(), planInput.getValidityDays(),
				planInput.getDataBenefits(), planInput.getCallBenefits(), planInput.getSmsBenefits(),planInput.getPlanType());

	    int planId = planDAO.createPlan(plan);

	    auditDAO.log(adminUserId,"RECHARGE_PLAN",planId,"CREATE",null,plan.getPlanName());

	    System.out.println("Recharge plan created successfully");
	}


	
	// used to update the plan price
	public void updatePlanPrice(String planName, double newPrice, int adminUserId) {

        double oldPrice = planDAO.getPlanPrice(planName);

        if (Double.compare(oldPrice, newPrice) == 0) {
            throw new RuntimeException("Price is already same");
        }

        planDAO.updatePlanPrice(planName, newPrice);
        
        int planId = planDAO.getPlanIdByName(planName);
        
        historyDAO.recordPriceChange(planId, oldPrice, newPrice, adminUserId);

        auditDAO.log(adminUserId, "RECHARGE_PLAN", planId, "PRICE_UPDATE", String.valueOf(oldPrice), 
        		String.valueOf(newPrice));

        System.out.println("Plan price updated successfully");
    }
	
	// used to change the plan status from active or deactive
	public void changePlanStatus(String planName, boolean active, int adminUserId) {

        planDAO.updatePlanStatus(planName, active);
        
        int planId = planDAO.getPlanIdByName(planName);

        auditDAO.log(adminUserId, "RECHARGE_PLAN", planId, active ? "ACTIVATE" : "DEACTIVATE", null, 
        		String.valueOf(active));

        System.out.println("Plan status updated");
    }
		
}
