package com.recharge;

import com.recharge.config.DBConnection;
import com.recharge.service.PaymentService;

public class PaymentTest {

	public static void main(String[] args) {
		
		DBConnection.initialize();
		
		PaymentService service = new PaymentService();
		boolean result1 = service.attemptPayment(7, 299.0);
		boolean result2 = service.attemptPayment(7, 299.0);
		
		DBConnection.close();
	}

}
