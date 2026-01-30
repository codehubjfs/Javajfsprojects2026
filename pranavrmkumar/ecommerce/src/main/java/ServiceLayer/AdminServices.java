package ServiceLayer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import DAO.AdminDAO;
import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.EntityNotFoundException;
import Exceptions.InputMismatchException;
import Model.CartItem;
import Model.Order;
import util.InputValidate;

public class AdminServices {
	
	public static void manageCustomers(Scanner s) {
		int choice = -1;
		do {
			try {
		System.out.println("1.View all customers.\n2.Block a Customer\n3.Go back to previous page.");
		System.out.print("Please enter your choice: ");
		choice = InputValidate.ChoiceValidation(s, 1, 3);
		System.out.println();
		switch(choice) {
		
		case 1:
			AdminDAO.viewCustomers().stream()
			.forEach(u -> 
					System.out.println("Name: " + u.getName() + " | " +"Email: " + u.getEmail() + " | " +"Status: " + u.getStatus()));
			System.out.println();
			break;
			
		case 2:
			String email = InputValidate.EmailValidation(s, "Enter email ID of customer to block: ");
			AdminDAO.blockCustomers(email);
			System.out.println("Customer is blocked");
			break;
			
		case 3:
			
			return;
			}	
			}catch(EmptyInputException | InputMismatchException | DBAccessException | EntityNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println();
			choice = -1;
		}
		}while(choice != 2);
		
	}
	
	
	public static void manageCategory(Scanner s) throws Exception{
		int choice = -1;
		do {
			try {
			System.out.println("1.View all Categories\n2.Create a new Category.\n3.Delete a Category\n4.Modify a category.\n5.Go back to previous page.");
			System.out.print("Please enter your choice: ");
			choice = InputValidate.ChoiceValidation(s, 1, 5);
			System.out.println();
			switch(choice) {
			
			case 1:
				AdminDAO.viewCategories().stream()
				.forEach(c -> 
						System.out.println("Category ID: " + c.getCategoryID() + " | " + "Category Name: " + c.getCategoryName() + " | " + "Description: " + c.getDescription() + " | " + "Status: " + c.getStatus()));
				System.out.println();
				break;
				
			case 2:
				String category_name = InputValidate.StringValidation(s, "Please enter the new Category name: ");
				String desc = InputValidate.DescURLValidation(s, "Please enter the Category Description: ");
				AdminDAO.addCategory(category_name, desc);
				System.out.println("New Category created Successfully");
				System.out.println();
				break;
				
			case 3:
				AdminDAO.viewCategories().stream()
				.forEach(c -> 
						System.out.println("Category ID: " + c.getCategoryID() + " | " + "Category Name: " + c.getCategoryName() + " | " + "Description: " + c.getDescription() + " | " + "Status: " + c.getStatus()));
				int c_id = InputValidate.IntValidation(s, "Please enter id of category to delete: ");
				AdminDAO.deleteCategory(c_id);
				System.out.println("Category is deleted.");
				System.out.println();
				break;
				
			case 4:
				int modify_choice = -1;
				int cat_id;
				do {
					try {
					System.out.println("1.Modify Category Name.\n2.Modify Category Description\n3.Modify Category Status.\n4.Go back.");
					System.out.print("Please enter your choice: ");
					modify_choice = InputValidate.ChoiceValidation(s, 1, 4);
					System.out.println();
					switch(modify_choice){
					
					case 1:
						AdminDAO.viewCategories().stream()
						.forEach(c -> 
								System.out.println("Category ID: " + c.getCategoryID() + " | " + "Category Name: " + c.getCategoryName() + " | " + "Description: " + c.getDescription() + " | " + "Status: " + c.getStatus()));
						cat_id = InputValidate.IntValidation(s, "Please enter Category_id to modify: ");
						String cat_name = InputValidate.StringValidation(s, "Please enter new Category name: ");
						AdminDAO.modifyCategoryName(cat_name, cat_id);
						System.out.println("Category name is modified successfully");
						break;
						
					case 2:
						AdminDAO.viewCategories().stream()
						.forEach(c -> 
								System.out.println("Category ID: " + c.getCategoryID() + " | " + "Category Name: " + c.getCategoryName() + " | " + "Description: " + c.getDescription() + " | " + "Status: " + c.getStatus()));
						cat_id = InputValidate.IntValidation(s, "Please enter Category_id to modify: ");
						String cat_desc = InputValidate.DescURLValidation(s, "Please enter new Category Description: ");
						AdminDAO.modifyCategoryDesc(cat_desc, cat_id);
						System.out.println("Category Description is modified successfully");
						break;
						
					case 3:
						AdminDAO.viewCategories().stream()
						.forEach(c -> 
								System.out.println("Category ID: " + c.getCategoryID() + " | " + "Category Name: " + c.getCategoryName() + " | " + "Description: " + c.getDescription() + " | " + "Status: " + c.getStatus()));
						cat_id = InputValidate.IntValidation(s, "Please enter Category_id to modify: ");
						String cat_status = InputValidate.StringValidation(s, "Please enter new Category Status: ").toLowerCase();
						while (!cat_status.equalsIgnoreCase("active") &&
							       !cat_status.equalsIgnoreCase("inactive")) {
							    System.out.println("status must be active or inactive");
							    cat_status = InputValidate.StringValidation(s, "Please enter new Category Status: ");
							}
						AdminDAO.modifyCategoryStatus(cat_status, cat_id);
						System.out.println("Category status is modified successfully");
						break;
					case 4:
						break;
					}}catch(EmptyInputException | InputMismatchException |DBAccessException e) {
						System.out.println(e.getMessage());
						System.out.println();
						modify_choice = -1;
					}
				}while(modify_choice != 4);
				break;
			case 5:
				return;
			}
			}catch(EmptyInputException | InputMismatchException |DBAccessException e) {
				System.out.println(e.getMessage());
				System.out.println();
				choice = -1;
			}
		}while(choice != 5);
	}
	
	
	
	
	
	public static void manageProducts(Scanner s) throws Exception{
		int choice = -1;
		int pid;
		do {
			try {
			System.out.println("1.Add a new Product.\n2.Delete a Product\n3.Modify price of a product.\n4.View all Products\n5.Go back to previous page.");
			System.out.println("Please enter your choice: ");
			choice = InputValidate.ChoiceValidation(s, 1, 5);
			System.out.println();
			switch(choice) {
			
			case 1:
				AdminDAO.viewCategories().stream()
				.forEach(c -> 
						System.out.println("Category ID: " + c.getCategoryID() + " | " + "Category Name: " + c.getCategoryName() + " | " + "Description: " + c.getDescription() + " | " + "Status: " + c.getStatus()));
				int cat_id = InputValidate.IntValidation(s, "Please enter Category_id of Product: ");
				String name = InputValidate.StringValidation(s, "Please enter the Product Name: ");
				String brand = InputValidate.StringValidation(s, "Please enter the Product Brand: ");
				String desc = InputValidate.DescURLValidation(s, "Please enter the Product Description: ");
				double price = InputValidate.DoubleValidation(s, "Please enter the Product Price: ", 1.0, 100000);
				String url = InputValidate.DescURLValidation(s, "Please enter the Product Image URL: ");
				AdminDAO.addProduct(cat_id, name, brand, desc, price, url);
				System.out.println("New Product added");
				System.out.println();
				break;
				
			case 2:
				AdminDAO.viewProducts().stream()
				.forEach(p -> 
						System.out.println("Product ID: " + p.getProductID() + " | " + "Category ID: " + p.getCategoryID() + " | " + "Product Name: " + p.getName() + " | " + "Brand: " + p.getBrand()
						 + " | " + "Price: " + p.getPrice() + " | " + "Description: " + p.getDescription() + " | " + "Image: " + p.getURL() + " | " + "Status: " + p.getStatus()));
				pid = InputValidate.IntValidation(s, "Please enter Product ID to delete: ");
				AdminDAO.deleteProduct(pid);
				System.out.println("Product deleted");
				break;
				
			case 3:
				AdminDAO.viewProducts().stream()
				.forEach(p -> 
						System.out.println("Product ID: " + p.getProductID() + " | " + "Category ID: " + p.getCategoryID() + " | " + "Product Name: " + p.getName() + " | " + "Brand: " + p.getBrand()
						 + " | " + "Price: " + p.getPrice() + " | " + "Description: " + p.getDescription() + " | " + "Image: " + p.getURL() + " | " + "Status: " + p.getStatus()));
				pid = InputValidate.IntValidation(s, "Please enter Product ID to update price: ");
				double newPrice = InputValidate.DoubleValidation(s, "Please enter new Price: ", 1.0, 100000.0);
				AdminDAO.modifyProductPrice(pid,newPrice);
				System.out.println("Price updated");
				break;
				
			case 4:
				AdminDAO.viewProducts().stream()
				.forEach(p -> 
						System.out.println("Product ID: " + p.getProductID() + " | " + "Category ID: " + p.getCategoryID() + " | " + "Product Name: " + p.getName() + " | " + "Brand: " + p.getBrand()
						 + " | " + "Price: " + p.getPrice() + " | " + "Description: " + p.getDescription() + " | " + "Image: " + p.getURL() + " | " + "Status: " + p.getStatus()));
				System.out.println();
				break;
				
			case 5:
				return;
			}}catch(EmptyInputException | InputMismatchException |DBAccessException e) {
				System.out.println(e.getMessage());
				System.out.println();
				choice = -1;
			}
		}while(choice != 5);
	}
	
	
	
	public static void manageInventory(Scanner s) throws Exception{
		int choice = -1;
		do {
			try {
			System.out.println("1.View Inventory.\n2.Add more stock to Inventory\n3.Go back to previous page.");
			System.out.println("Please enter your choice: ");
			choice = InputValidate.ChoiceValidation(s, 1, 3);
			System.out.println();
			switch(choice) {
			
			case 1:
				
				AdminDAO.viewInventory().stream()
				.forEach(i ->
						System.out.println("Inventory ID: " + i.getInventoryID() + " | " + "Product ID: " + i.getProductID() + " | " + "Product Name: " + i.getPName() + " | " + "Stock Quantity: "+i.getQuantity()));
				System.out.println();
				break;
				
			case 2:
				AdminDAO.viewInventory().stream()
				.forEach(i ->
						System.out.println("Inventory ID: " + i.getInventoryID() + " | " + "Product ID: " + i.getProductID() + " | " + "Product Name: " + i.getPName() + " | " + "Stock Quantity: "+i.getQuantity()));
				int pid = InputValidate.IntValidation(s, "Please enter Product ID to update stock: ");
				
				System.out.println();
				int quantity = InputValidate.IntValidation(s, "Enter quantity to add: ");
				
				AdminDAO.updateInventory(quantity, pid);
				System.out.println("Stock Updated");
				break;
				
			case 3:
				return;
			}
			}catch(EmptyInputException | InputMismatchException |DBAccessException e) {
				System.out.println(e.getMessage());
				System.out.println();
				choice = -1;
			}
		}while(choice != 3);
	}




	public static void manageDiscounts(Scanner s) throws Exception{
		// TODO Auto-generated method stub
		int choice = -1;
		do {
			try {
		System.out.println("1.Add new Discount\n2.Delete discount.\n3.View all discounts.\n4.Go back to previous page.");
		System.out.println("Please enter your choice: ");
		choice = InputValidate.ChoiceValidation(s, 1, 4);
		System.out.println();
		switch(choice) {
		
		case 1:
			String code = InputValidate.DescURLValidation(s, "Enter promo code: ");
			double percent = InputValidate.DoubleValidation(s, "Enter discount percentage: ", 0.0, 100.0);

			String expiry_date = InputValidate.DateValidation(s, "Enter expiry date (yyyy-mm-dd): ");
			LocalDate expiry = LocalDate.parse(expiry_date);
			LocalDate today = LocalDate.now();

			if (expiry.isBefore(today)) {
			    throw new DBAccessException("Expiry date cannot be before today.");
			}

			AdminDAO.createDiscount(code,percent,expiry_date);
			System.out.println("New Discount Added");
			break;
			
		case 2:
			AdminDAO.viewDiscounts().stream()
			.forEach(d -> 
			System.out.println("Discount ID: " + d.getDID() + " | " + "Promo Code: " + d.getCode() + " | " + "Discount Percentage: " + d.getDPT() + " | " + "Expiry Date: " +  d.getExpDate() + " | " + "Status: " + d.getStatus()));
			int did = InputValidate.IntValidation(s, "Enter discount id: ");
			AdminDAO.deleteDiscount(did);
			System.out.println("Discount Deleted");
			break;
			
		case 3:
			AdminDAO.viewDiscounts().stream()
			.forEach(d -> 
			System.out.println("Discount ID: " + d.getDID() + " | " + "Promo Code: " + d.getCode() + " | " + "Discount Percentage: " + d.getDPT() + " | " + "Expiry Date: " +  d.getExpDate() + " | " + "Status: " + d.getStatus()));
			System.out.println();
            break;
            
		case 4:
			return;
		}
			}catch(EmptyInputException | InputMismatchException |DBAccessException e) {
				System.out.println(e.getMessage());
				System.out.println();
				choice = -1;
			}
		}while(choice != 4);
	}
	
	public static void manageTickets(Scanner s) throws Exception {
		int choice = -1;
	    do {
	    	try {
	    	System.out.println("1.View all Support Tickets.\n2.Update Ticket Status.\n3.Go back to previous page.");
	        System.out.print("Please enter your choice: ");

	        choice = InputValidate.ChoiceValidation(s, 1, 3);
	        System.out.println();

	        switch (choice) {
	        
	            case 1:
	                AdminDAO.viewTickets().stream()
	                .forEach(t -> 
	                System.out.println("Ticket ID: " + t.getTID() + " | " + "User ID: " + t.getUID() + " | " + "Order ID: " + t.getOID()
	                + " | " + "Issue Type: " + t.getIssueType() + " | " + "Description: " +  t.getDesc() + " | " + "Ticket Status: " + t.getTicketStatus() + " | " + "Created Date: " + t.getDate()));
	                break;

	            case 2:
	                int tid = InputValidate.IntValidation(s, "Enter Ticket id to Update: ");
	                String status = InputValidate.StringValidation(s, "Enter status (open/closed): ").toLowerCase();
	                while (!status.matches("open|in_progress|resolved|closed")) {
	                    System.out.println("Invalid status. Choose: open/in_progress/resolved/closed");
	                }
	                AdminDAO.updateTicketStatus(status, tid);
	                System.out.println("ticket updated");
	                break;

	            case 3:
	                return;
	        }
	    	}catch(EmptyInputException | InputMismatchException |DBAccessException e) {
				System.out.println(e.getMessage());
				System.out.println();
				choice = -1;
			}
	    } while (choice != 3);
	}
	
	
	public static void manageOrders(Scanner s) throws Exception{

	    int choice = -1;

	    do {
	        try {
	            System.out.println("1.View all Orders\n2.View Order Details\n3.Go back to previous page.");
	            System.out.print("Please enter your choice: ");

	            choice = InputValidate.ChoiceValidation(s, 1, 3);
	            System.out.println();

	            switch (choice) {

	                case 1:
	                    ArrayList<Order> orders = AdminDAO.viewOrders();
	                    if (orders.isEmpty()) {
	                        System.out.println("No orders found.");
	                    } else {
	                        for (Order order : orders) {
	                            order.displayAdminOrder();
	                        }
	                    }
	                    System.out.println();
	                    break;

	                case 2:
	                    int orderId = InputValidate.IntValidation(s, "Enter order ID to view details: ");
	                    ArrayList<CartItem> items = AdminDAO.viewOrderDetails(orderId);

	                    if (items.isEmpty()) {
	                        System.out.println("No items found for this order.");
	                    } else {
	                        System.out.println("----------- Order " + orderId + " Details -----------");
	                        double total = 0;

	                        for (CartItem item : items) {
	                            System.out.println(
	                                "Product: " + item.getProduct().getName() +
	                                "\nBrand: " + item.getProduct().getBrand() +
	                                "\nQty: " + item.getQuantity() +
	                                "\nPrice: ₹" + item.getPrice() +
	                                "\nTotal: ₹" + item.getItemTotal()
	                            );
	                            total += item.getItemTotal();
	                        }

	                        System.out.println("Order Total: ₹" + total);
	                        System.out.println("================================");
	                    }

	                    System.out.println();
	                    break;

	                case 3:
	                    return;
	            }

	        } catch (EmptyInputException | InputMismatchException | DBAccessException e) {
	            System.out.println(e.getMessage());
	            System.out.println();
	            choice = -1;
	        }

	    } while (choice != 3);
	}


}
