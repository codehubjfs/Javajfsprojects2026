package UI;
import java.util.Scanner;

import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import ServiceLayer.AdminServices;
import util.InputValidate;

public class Admin {
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		int choice;
		System.out.println("--------------------------Welcome to Admin Page--------------------------------");
		try {
		do {
		while(true) {
			try {
			System.out.println("1.Manage Customers.\n2.Manage Categories\n3.Manage Products\n4.Manage Inventory\n"
					+ "5.Manage Discounts\n6.Manage Support Tickets.\n7.Logout");
			System.out.print("Please enter your choice: ");
			choice = InputValidate.ChoiceValidation(s, 1, 7);
			break;
			}catch(EmptyInputException | InputMismatchException e) {
				System.out.println(e.getMessage());
				System.out.println();
			}
		}
		System.out.println();
		switch(choice) {
			case 1:
				AdminServices.manageCustomers();
				break;
			case 2:
				AdminServices.manageCategory();
				break;
			case 3:
				AdminServices.manageProducts();
				break;
			case 4:
				AdminServices.manageInventory();
				break;
			case 5:
				AdminServices.manageDiscounts();
				break;
			case 6:
				AdminServices.manageTickets();
				break;
			case 7:
				System.out.println("Thank You Admin!");
				break;
			default:
				System.out.println("Please choose a valid option.");
		}
		System.out.println();
		}while(choice!=7);
		}
		catch(Exception e) {
			System.out.println("An error has occured: "+e.getMessage());
		}
		finally {
		s.close();
		}
	}
}
