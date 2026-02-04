package com.recharge.service;

import com.recharge.dao.AuditLogDAO;
import com.recharge.dao.OperatorDAO;

public class AdminOperatorService {
	
	private final OperatorDAO operatorDAO = new OperatorDAO();
	private final AuditLogDAO auditLogDAO = new AuditLogDAO();
	
	// add operator to the DB
	public void addOperator(String operatorName, int adminUserId) {
		
		if(operatorDAO.operatorExists(operatorName)) {
			throw new RuntimeException("Operator already exists");
		}
		
		int operatorId = operatorDAO.addOperator(operatorName);
		auditLogDAO.log(adminUserId, "OPERATOR", operatorId, "CREATE", null, "ACTIVE");
		
		System.out.println("Operator added successfully");
	}
	
	// change operator status active or inactive
	public void changeOperatorStatusByName(String operatorName, String newStatus,int adminUserId) {

        int operatorId = operatorDAO.getOperatorIdByName(operatorName);
        String currentStatus = operatorDAO.getOperatorStatus(operatorId);

        if (currentStatus.equalsIgnoreCase(newStatus)) {
            throw new RuntimeException("Operator already in " + newStatus + " state");
        }

        operatorDAO.updateOperatorStatus(operatorId, newStatus);

        auditLogDAO.log(adminUserId,"OPERATOR",operatorId,newStatus.equalsIgnoreCase("ACTIVE") ? "ACTIVATE" : "DEACTIVATE",
        		currentStatus,newStatus);

        System.out.println("Operator status updated");
    }
}
