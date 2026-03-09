package com.ecommerce.ui;

import java.util.Scanner;

import com.ecommerce.exceptions.DBAccessException;
import com.ecommerce.exceptions.EmptyInputException;
import com.ecommerce.exceptions.InputMismatchException;
import com.ecommerce.service.CustomerServices;
import com.ecommerce.util.InputValidate;

public class CustomerMenu {

	public static void customerMenu(Scanner s,com.ecommerce.models.Customer customer) {
		int choice = -1;
		System.out.println();
		do {
			try {
				System.out.println("1.View Products\n2.View Cart\n3.Checkout\n4.View Orders\n5.Cancel an Order\n6.Profile Management\n7.View Discounts\n8.Submit a Support Ticket\n9.Logout");
				System.out.print("Enter your choice: ");
	            choice = InputValidate.ChoiceValidation(s, 1, 9);
	            System.out.println();
	            switch(choice) {
	            case 1:
	            	CustomerServices.viewProductsMenu(s,customer);
	            	break;
	            case 2:
	            	CustomerServices.viewCart(customer);
	            	break;
	            case 3:
	            	CustomerServices.checkout(s,customer);
	                break;
	            case 4:
	            	CustomerServices.viewOrders(customer);
	            	break;
	            case 5:
	            	CustomerServices.cancelOrder(s,customer);
	            case 6:
	            	CustomerServices.profileMenu(s,customer);
	            	break;
	            case 7:
	            	CustomerServices.viewDiscounts();
	            	break;
	            case 8:
	            	CustomerServices.submitSupportTicket(s, customer); 
	            	break;
	            case 9:
	            	System.out.println("Logged Out Successfully");
	            	System.out.println();
	            	return;
	            }
			}catch (EmptyInputException | InputMismatchException e) {
                System.out.println(e.getMessage());
            } catch (DBAccessException e) {
				System.out.println(e.getMessage());
				System.out.println();
			}
		}while(choice != 9);
	}
}
