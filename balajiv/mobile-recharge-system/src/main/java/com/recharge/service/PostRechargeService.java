package com.recharge.service;

import com.recharge.dao.NotificationDAO;
import com.recharge.dao.RechargeInvoiceDAO;
import com.recharge.model.Notification;
import com.recharge.model.RechargeInvoice;

public class PostRechargeService {
	
	private final RechargeInvoiceDAO invoiceDAO = new RechargeInvoiceDAO();
	private final NotificationDAO notificationDAO = new NotificationDAO();
	
	/**
	 * used to handle successful recharge 
	 * @param rechargeId
	 * @param userId
	 */
	public void handleSuccessfulRecharge(int rechargeId, int userId) {
		
		//generate invoice
		String invoiceUrl = "Invoice-" + rechargeId + ".pdf";
		RechargeInvoice invoice = new RechargeInvoice(rechargeId, invoiceUrl);
		invoiceDAO.createInvoice(invoice);
		
		//notify user
		Notification notification = new Notification(userId, "Recharge", "Your recharge was successfully done. Invoice: "+invoiceUrl);
		notificationDAO.createNotification(notification);
	}
}
