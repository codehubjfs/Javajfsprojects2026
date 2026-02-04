package com.recharge.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TransactionRefUtil {
	
	private TransactionRefUtil() {
		// prevent instantiation
	}
	
	/*
	 * used to generate the transaction reference
	 */
	public static String generate() {
		return "TXN-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
				+"-"+UUID.randomUUID().toString().substring(0,4).toUpperCase();
		
	}
}
