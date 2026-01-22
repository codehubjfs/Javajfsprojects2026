package ServiceLayer;
import java.util.Scanner;

import DAO.AdminDAO;

public class AdminServices {
	static Scanner s = new Scanner(System.in);
	static int choice;
	
	
	public static void manageCustomers() throws Exception {
		do {
		System.out.println("1.View all customers.\n2.Go back to previous page.");
		System.out.print("Please enter your choice: ");
		choice = s.nextInt();
		System.out.println();
		switch(choice) {
		case 1:
			AdminDAO.viewCustomers();
			System.out.println();
			break;
		case 2:
			return;
		}	
		}while(choice != 2);
	}
	
	
	
	
	public static void manageCategory() throws Exception{
		do {
			System.out.println("1.View all Categories\n2.Create a new Category.\n3.Delete a Category\n4.Modify a category.\n5.Go back to previous page.");
			System.out.print("Please enter your choice: ");
			choice = s.nextInt();
			s.nextLine();
			System.out.println();
			switch(choice) {
			case 1:
				AdminDAO.viewCategories();
				System.out.println();
				break;
			case 2:
				System.out.println("Please enter the new Category name: ");
				String category_name = s.nextLine();
				System.out.println("Please enter the Category Description: ");
				String desc = s.nextLine();
				AdminDAO.addCategory(category_name, desc);
				System.out.println();
				break;
			case 3:
				System.out.println("Please enter name of category to delete: ");
				String delete_name = s.nextLine();
				AdminDAO.deleteCategory(delete_name);
				System.out.println(delete_name + " category is deleted.");
				System.out.println();
				break;
			case 4:
				int modify_choice;
				int cat_id;
				do {
					System.out.println("1.Modify Category Name.\n2.Modify Category Description\n3.Modify Category Status.\n4.Go back.");
					System.out.print("Please enter your choice: ");
					modify_choice = s.nextInt();
					s.nextLine();
					System.out.println();
					switch(modify_choice){
					case 1:
						System.out.println("Please enter Category_id to modify: ");
						cat_id = s.nextInt();
						s.nextLine();
						System.out.println("Please enter new Category name: ");
						String cat_name = s.nextLine();
						AdminDAO.modifyCategoryName(cat_name, cat_id);
						break;
					case 2:
						System.out.println("Please enter Category_id to modify: ");
						cat_id = s.nextInt();
						s.nextLine();
						System.out.println("Please enter new Category Description: ");
						String cat_desc = s.nextLine();
						AdminDAO.modifyCategoryDesc(cat_desc, cat_id);
						break;
					case 3:
						System.out.println("Please enter Category_id to modify: ");
						cat_id = s.nextInt();
						s.nextLine();
						System.out.println("Please enter new Category Status: ");
						String cat_status = s.nextLine();
						while (!cat_status.equalsIgnoreCase("active") &&
							       !cat_status.equalsIgnoreCase("inactive")) {
							    System.out.println("status must be active or inactive");
							    cat_status = s.nextLine();
							}
						AdminDAO.modifyCategoryStatus(cat_status, cat_id);
						break;
					case 4:
						break;
					}
				}while(modify_choice != 4);
				break;
			case 5:
				return;
			}
		}while(choice != 5);
	}
	
	
	
	
	
	public static void manageProducts() throws Exception{
		int pid;
		do {
			System.out.println("1.Add a new Product.\n2.Delete a Product\n3.Modify price of a product.\n4.View all Products\n5.Go back to previous page.");
			System.out.println("Please enter your choice: ");
			choice = s.nextInt();
			System.out.println();
			switch(choice) {
			case 1:
				System.out.print("Enter the Category ID of the Product: ");
				int cat_id = s.nextInt();
				s.nextLine();
				System.out.print("Please enter the Product Name: ");
				String name = s.nextLine();
				System.out.print("Please enter the Product Brand: ");
				String brand = s.nextLine();
				System.out.print("Please enter the Product Description: ");
				String desc = s.nextLine();
				System.out.print("Please enter the Product Price: ");
				double price = s.nextDouble();
				s.nextLine();
				System.out.print("Please enter the Product Image URL: ");
				String url = s.nextLine();
				AdminDAO.addProduct(cat_id, name, brand, desc, price, url);
				System.out.println();
				break;
			case 2:
				System.out.println("Please enter Product ID to delete: ");
				pid = s.nextInt();
				s.nextLine();
				AdminDAO.deleteProduct(pid);
				System.out.println("Product deleted");
				break;
			case 3:
				System.out.println("Please enter Product ID to update price: ");
				pid = s.nextInt();
				s.nextLine();
				System.out.println("Please enter new Price: ");
				double newPrice = s.nextDouble();
				s.nextLine();
				AdminDAO.modifyProductPrice(pid,newPrice);
				System.out.println("Price updated");
				break;
			case 4:
				AdminDAO.viewProducts();
				System.out.println();
				break;
			case 5:
				return;
			}
		}while(choice != 5);
	}
	
	
	
	public static void manageInventory() throws Exception{
		do {
			System.out.println("1.Check Inventory of a Product.\n2.Add more stock to Inventory\n3.Go back to previous page.");
			System.out.println("Please enter your choice");
			choice = s.nextInt();
			s.nextLine();
			System.out.println();
			switch(choice) {
			case 1:
				AdminDAO.viewInventory();
				break;
			case 2:
				System.out.println("Please enter Product ID to update stock: ");
				int pid = s.nextInt();
				s.nextLine();
				
				System.out.println("Enter quantity to add: ");
				int quantity = s.nextInt();
				s.nextLine();
				
				AdminDAO.updateInventory(quantity, pid);
				System.out.println("Stock Updated");
				break;
				
			case 3:
				return;
			}
		}while(choice != 3);
	}




	public static void manageDiscounts() throws Exception{
		// TODO Auto-generated method stub
		do {
		System.out.println("1.Add new Discount\n2.Delete discount.\n3.View all discounts.\n4Go back to previous page.");
		System.out.println("Please enter your choice: ");
		choice = s.nextInt();
		s.nextLine();
		System.out.println();
		switch(choice) {
		case 1:
			System.out.println("Enter promo code: ");
			String code = s.nextLine();
			System.out.println("Enter discount percentage: ");
			double percent = s.nextDouble();
			s.nextLine();
			String expiry_date;
			while(true) {
				System.out.println("Enter expiry date (yyyy-mm-dd): ");
				expiry_date = s.nextLine();
				if(expiry_date.matches("\\d{4}-\\d{2}-\\d{2}")) {
					break;
				}
				System.out.println("Invalid date format. Use yyyy-mm-dd");
			}
			AdminDAO.createDiscount(code,percent,expiry_date);
			System.out.println("Discount Added");
			break;
			
		case 2:
			System.out.print("Enter discount id: ");
			int did = s.nextInt();
			s.nextLine();
			AdminDAO.deleteDiscount(did);
			System.out.println("Discount Deleted");
			break;
			
		case 3:
			AdminDAO.viewDiscounts();
            break;
            
		case 4:
			return;
		}
		
		}while(choice != 4);
	}
	
	public static void manageTickets() throws Exception {
	    do {
	        System.out.println("1.View Tickets");
	        System.out.println("2.Update Ticket Status");
	        System.out.println("3.Go back");
	        System.out.print("enter choice: ");

	        choice = s.nextInt();
	        s.nextLine();
	        System.out.println();

	        switch (choice) {
	            case 1:
	                AdminDAO.viewTickets();
	                break;

	            case 2:
	                System.out.print("enter ticket id: ");
	                int tid = s.nextInt();
	                s.nextLine();

	                System.out.print("enter status (open/closed): ");
	                String status = s.nextLine();

	                AdminDAO.updateTicketStatus(status, tid);
	                System.out.println("ticket updated");
	                break;

	            case 3:
	                return;
	        }
	    } while (choice != 3);
	}

}
