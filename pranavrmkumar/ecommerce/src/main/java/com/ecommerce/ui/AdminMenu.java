package com.ecommerce.ui;
import java.util.Scanner;

import com.ecommerce.exceptions.EmptyInputException;
import com.ecommerce.exceptions.InputMismatchException;
import com.ecommerce.service.AdminServices;
import com.ecommerce.util.InputValidate;

public class AdminMenu {
	public static void menu(Scanner s) throws Exception {
		int choice;
		System.out.println("--------------------------Welcome to Admin Page--------------------------------");
		try {
		do {
		while(true) {
			try {
			System.out.println("1.Manage Customers.\n2.Manage Categories\n3.Manage Products\n4.Manage Inventory\n"
					+ "5.Manage Discounts\n6.Manage Support Tickets.\n7.Manage Orders\n8.Manage Payments\n9.Logout");
			System.out.print("Please enter your choice: ");
			choice = InputValidate.ChoiceValidation(s, 1, 9);
			break;
			}catch(EmptyInputException | InputMismatchException e) {
				System.out.println(e.getMessage());
				System.out.println();
			}
		}
		System.out.println();
		switch(choice) {
			case 1:
				AdminServices.manageCustomers(s);
				break;
			case 2:
				AdminServices.manageCategory(s);
				break;
			case 3:
				AdminServices.manageProducts(s);
				break;
			case 4:
				AdminServices.manageInventory(s);
				break;
			case 5:
				AdminServices.manageDiscounts(s);
				break;
			case 6:
				AdminServices.manageTickets(s);
				break;
			case 7:
				AdminServices.manageOrders(s);
				break;
			case 8:
				AdminServices.managePayments();
				break;
			case 9:
				System.out.println("Thank You Admin!");
				System.out.println();
				return;
			default:
				System.out.println("Please choose a valid option.");
		}
		System.out.println();
		}while(choice!=9);
		}
		catch(Exception e) {
			System.out.println("An error has occured: "+e.getMessage());
		}

	}
}
