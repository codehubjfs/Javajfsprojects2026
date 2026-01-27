package UI;

import java.util.Scanner;

import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import ServiceLayer.CustomerServices;
import util.InputValidate;

public class Customer {

	public static void customerMenu(Model.Customer customer) {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		int choice = -1;
		System.out.println("Welcome "+customer.getName());
		System.out.println();
		do {
			try {
				System.out.println("1.View Products\n2.View Cart\n3.Checkout\n4.View Orders\n5.Logout");
				System.out.print("Enter your choice: ");
	            choice = InputValidate.ChoiceValidation(s, 1, 5);
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
	            	System.out.println("Logged Out Successfully");
	            	LoginUI.main(null);
	            	break;
	            }
			}catch (EmptyInputException | InputMismatchException e) {
                System.out.println(e.getMessage());
            } catch (DBAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(choice != 5);
	}
}
