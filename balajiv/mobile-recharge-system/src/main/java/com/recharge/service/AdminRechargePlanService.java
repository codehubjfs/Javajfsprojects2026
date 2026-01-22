package com.recharge.service;

import com.recharge.dao.AuditLogDAO;
import com.recharge.dao.OperatorDAO;
import com.recharge.dao.PlanUpdationHistoryDAO;
import com.recharge.dao.RechargePlanDAO;
import com.recharge.model.RechargePlan;

public class AdminRechargePlanService {

	RechargePlanDAO planDAO = new RechargePlanDAO();
	PlanUpdationHistoryDAO historyDAO = new PlanUpdationHistoryDAO();
	AuditLogDAO auditDAO = new AuditLogDAO();
	OperatorDAO operator = new OperatorDAO();

	
	public void createPlan(RechargePlan plan, int adminUserId) {
		
		if(operator.getOperatorStatus(plan.getOperatorId()) == "ACTIVE") {	
			
			int planId = planDAO.createPlan(plan);
			
			auditDAO.log(adminUserId, "RECHARGE_PLAN", planId, "CREATE", null, plan.getPlanName());
			
			System.out.println("Recharge plan created successfully");
		}
		else {
			System.out.println("You can't create the recharge plan for INACTIVE Operators");
		}
    }
	
	public void updatePlanPrice(int planId, double newPrice, int adminUserId) {

        double oldPrice = planDAO.getPlanPrice(planId);

        if (Double.compare(oldPrice, newPrice) == 0) {
            throw new RuntimeException("Price is already same");
        }

        planDAO.updatePlanPrice(planId, newPrice);
        historyDAO.recordPriceChange(planId, oldPrice, newPrice, adminUserId);

        auditDAO.log(adminUserId, "RECHARGE_PLAN", planId, "PRICE_UPDATE", String.valueOf(oldPrice), 
        		String.valueOf(newPrice));

        System.out.println("Plan price updated successfully");
    }
	
	public void changePlanStatus(int planId, boolean active, int adminUserId) {

        planDAO.updatePlanStatus(planId, active);

        auditDAO.log(adminUserId, "RECHARGE_PLAN", planId,active ? "ACTIVATE" : "DEACTIVATE", null, 
        		String.valueOf(active));

        System.out.println("Plan status updated");
    }
		
}
