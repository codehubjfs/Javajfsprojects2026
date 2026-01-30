package UI;

import java.util.Scanner;

import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import ServiceLayer.CustomerServices;
import util.InputValidate;

public class CustomerMenu {

	public static void customerMenu(Scanner s,Model.Customer customer) {
		// TODO Auto-generated method stub
		int choice = -1;
		System.out.println();
		do {
			try {
				System.out.println("1.View Products\n2.View Cart\n3.Checkout\n4.View Orders\n5.Profile Management\n6.Logout");
				System.out.print("Enter your choice: ");
	            choice = InputValidate.ChoiceValidation(s, 1, 6);
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
	            	CustomerServices.profileMenu(s,customer);
	            case 6:
	            	System.out.println("Logged Out Successfully");
	            	System.out.println();
	            	return;
	            }
			}catch (EmptyInputException | InputMismatchException e) {
                System.out.println(e.getMessage());
            } catch (DBAccessException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				System.out.println();
			}
		}while(choice != 6);
	}
}
