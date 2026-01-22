package com.util;

import com.enums.NotificationChannel;
import com.enums.NotificationType;

public final class NotificationUtil {
	
	private NotificationUtil() {}
	
	public static NotificationChannel resolveChannel(NotificationType eventType) {

        switch (eventType) {

            case PAYMENT_SUCCESS:
            case PAYMENT_FAILED:
            case INVOICE_GENERATED:
            case REFUND_INITIATED:
            case REFUND_SUCCESS:
            case REFUND_FAILED:
            case USER_REGISTERED:
                return NotificationChannel.EMAIL;

        
            case ORDER_PLACED:
            case ORDER_SHIPPED:
            case ORDER_DELIVERED:
            case ORDER_CANCELLED:
                return NotificationChannel.SMS;

 
            case LOW_STOCK:
            case OUT_OF_STOCK:
            case HIGH_SALES:
                return NotificationChannel.EMAIL;

            default:
                throw new IllegalArgumentException(
                        "Unsupported notification type: " + eventType
                );
        }
    }
}
