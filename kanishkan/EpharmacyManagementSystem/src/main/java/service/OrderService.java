package service;
import  java.util.Scanner;
import dao.OrderDao;

public class OrderService {
	static Scanner scanner = new Scanner(System.in);
	public static void updateOrderStatus() throws Exception{
		
		String values = "<html>1.placed"
				+ "2.Shipped"
				+ "3.Delivered"
				+ "4.cancelled"
				+ "</html>";
		System.out.print(values);
		System.out.println("Enter order id: ");
		int orderId = scanner.nextInt();
		System.out.println("Enter the status option to be updated: ");
		int statusNumber = scanner.nextInt();
		
		OrderDao.updateOrderStatus(statusNumber, orderId);
		
		
	}
	
	public static void cancelOrder() throws Exception{
		System.out.println("Enter Order Id to be cancelled: ");
		int orderId = scanner.nextInt();
		OrderDao.cancelOrder(orderId);
	}
}
