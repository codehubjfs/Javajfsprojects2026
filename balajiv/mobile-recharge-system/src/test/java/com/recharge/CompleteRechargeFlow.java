package com.recharge;

import com.recharge.config.DBConnection;
import com.recharge.service.RechargeOrchestratorService;

public class CompleteRechargeFlow {

	public static void main(String[] args) {
		
		DBConnection.initialize();
		RechargeOrchestratorService service = new RechargeOrchestratorService();

		boolean result = service.performRecharge(3, 9, 2, 299.0);

		System.out.println("Final Result: " + result);
	}
}
