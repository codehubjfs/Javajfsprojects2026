package ServiceLayer;
import java.util.Scanner;

public class Admin {
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		String c;
		int choice = 0;
		System.out.println("--------------------------Welcome to Admin Page--------------------------------");
		do {
		System.out.println("1.Manage Customers.\n2.Manage Categories\n3.Manage Products\n4.Manage Inventory\n"
				+ "5.Manage Discounts\n6.Manage Support Tickets.\n7.Logout");
		System.out.print("Please enter your choice: ");
		c = s.nextLine();
		System.out.println();
		if(!c.matches("^[0-9]+$")) {
			System.out.println("Invalid Input.Try Again");
			System.out.println();
			continue;
		}
		choice = Integer.parseInt(c);
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
		s.close();
	}
}
