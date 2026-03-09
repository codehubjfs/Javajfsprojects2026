package com.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.ecommerce.dao.CustomerDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.exceptions.DBAccessException;
import com.ecommerce.exceptions.EmptyInputException;
import com.ecommerce.exceptions.EntityNotFoundException;
import com.ecommerce.exceptions.InputMismatchException;
import com.ecommerce.models.Address;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Customer;
import com.ecommerce.models.Discount;
import com.ecommerce.models.Order;
import com.ecommerce.models.Product;
import com.ecommerce.util.InputValidate;

public class CustomerServices {
	
	public static void viewProductsMenu(Scanner s, Customer customer) {

	    try {
	        List<Product> products = ProductDAO.getActiveProducts();
	        if (products.isEmpty()) {
	            System.out.println("No products available.");
	            return;
	        }

	        int choice;
	        do {
	            System.out.println("1. View all products\n2. Filter by name\n3. Filter by brand\n4. Filter by price range\n5. Sort by price\n6. Back");
	            System.out.print("Enter choice: ");
	            choice = InputValidate.ChoiceValidation(s, 1, 6);
	            System.out.println();

	            List<Product> shownProducts = new ArrayList<>();

	            switch (choice) {

	                case 1:
	                    shownProducts = products;
	                    break;

	                case 2:
	                    String name = InputValidate.StringValidation(s, "Enter product name: ");
	                    shownProducts = products.stream()
	                            .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
	                            .collect(Collectors.toList());
	                    break;

	                case 3:
	                    String brand = InputValidate.StringValidation(s, "Enter brand: ");
	                    shownProducts = products.stream()
	                            .filter(p -> p.getBrand().equalsIgnoreCase(brand))
	                            .collect(Collectors.toList());
	                    break;

	                case 4:
	                    double min = InputValidate.DoubleValidation(
	                            s, "Enter min price:", 0, Double.MAX_VALUE);
	                    double max ;
	                    while(true) {
	                    	max = InputValidate.DoubleValidation(s, "Enter max price: ", 0, Double.MAX_VALUE);
	                    	
	                    	if(max < min) {
	                    		throw new InputMismatchException("Max value cannot be lesser than min value.");
	                    	}
	                    	else {
	                    		break;
	                    	}
	                    }
	                    shownProducts = products.stream()
	                            .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
	                            .collect(Collectors.toList());
	                    break;

	                case 5:
	                    System.out.println("1. Ascending\n2. Descending");
	                    int sortChoice = InputValidate.ChoiceValidation(s, 1, 2);
	                    shownProducts = products.stream()
	                            .sorted((p1,p2) -> {
	                            	if(sortChoice == 1) {
	                            		return Double.compare(p1.getPrice(),p2.getPrice());
	                            	}else {
	                            		return Double.compare(p2.getPrice(), p1.getPrice());
	                            	}
	                            })
	                            .collect(Collectors.toList());
	                    break;

	                case 6:
	                    return;
	            }

	            display(shownProducts);

	            if (!shownProducts.isEmpty()) {
	                System.out.print("Add a product to cart? (y/n): ");
	                String input = s.nextLine().trim();
	                char ch = input.charAt(0);


	                if (ch == 'y' || ch == 'Y' || input.equalsIgnoreCase("yes")) {
	                	int productId = InputValidate.IntValidation(s, "Enter Product ID:");
	                    addToCart(s,customer,productId);
	                }
	            }

	        } while (choice != 6);

	    } catch (DBAccessException | EmptyInputException | InputMismatchException e) {
	        System.out.println(e.getMessage());
	    }
	}


    private static void display(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No matching products found.\n");
            return;
        }

        products.forEach(p -> System.out.println(
                "Product ID: " + p.getProductID() + " | " +
                "Name: " + p.getName() + " | " +
                "Brand: " + p.getBrand() + " | " +
                "Description: " + p.getDescription() + " | " +
                "Image: " + p.getURL() + " | " +
                "Price: " + " ₹ " +p.getPrice()
        ));
        System.out.println();
    }

    
    
    

    public static void addToCart(Scanner s,Customer customer, int productId)
            throws DBAccessException {

        Product product = ProductDAO.getProductById(productId);

        if (product == null) {
            System.out.println("Product not Found");
            return;
        }

        int stock = CustomerDAO.getStock(productId);

        while (true) {
            try {
                System.out.print("Enter quantity (Available: " + stock + "): ");
                int quantity = InputValidate.IntValidation(s, "Enter quantity(Available: "+stock+"): ");
                s.nextLine();

                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }

                if (quantity > stock) {
                    System.out.println("Only " + stock + " items available. Please enter again.");
                    continue;
                }

                int cartId = CustomerDAO.getOrCreateCart(customer.getEmail());
                CustomerDAO.addOrUpdateCartItem(cartId, product, quantity);
                System.out.println("Item added to cart successfully!");
                break;

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                s.nextLine();
            }
        }
    }


    
    
    
    
    
	public static void viewCart(Customer customer) throws DBAccessException {

	    int cartId = CustomerDAO.getOrCreateCart(customer.getEmail());
	    CustomerDAO.refreshCartPrices(cartId);
	    ArrayList<CartItem> items = CustomerDAO.viewCart(cartId);

	    if (items.isEmpty()) {
	        System.out.println("Cart is empty.");
	        System.out.println();
	        return;
	    }

	    double total = 0;
	    for (CartItem item : items) {
	        System.out.println(
	                item.getProduct().getName() + " | Qty: " +
	                item.getQuantity() + " | ₹" +
	                item.getItemTotal()
	        );
	        total += item.getItemTotal();
	    }

	    System.out.println("Total Amount: ₹" + total);
	    System.out.println();
	}


	
	
	public static void checkout(Scanner s, Customer customer) throws DBAccessException, EmptyInputException, InputMismatchException {

	    int cartId = CustomerDAO.getOrCreateCart(customer.getEmail());
	    CustomerDAO.refreshCartPrices(cartId);
	    ArrayList<CartItem> items = CustomerDAO.viewCart(cartId);

	    if (items.isEmpty()) {
	        System.out.println("Cart is empty. Cannot checkout.");
	        return;
	    }
	    
	    for(CartItem item: items) {
	    	if(!ProductDAO.isProductActive(item.getProduct().getProductID())) {
	    		System.out.println("Product "+item.getProduct().getName()+" is no longer available");
	    		return;
	    	}
	    }
	    double total = 0;
	    for (CartItem item : items) {
	        System.out.println(
	                item.getProduct().getName() +
	                " | Qty: " + item.getQuantity() +
	                " | ₹" + item.getItemTotal()
	        );
	        total += item.getItemTotal();
	    }
	    
	    List<Address> addresses = UserDAO.getAddressesByUserId(UserDAO.getUserIdByEmail(customer.getEmail()));
	    if (addresses.isEmpty()) {
	        System.out.println("No addresses found. Please add one in Profile Management.");
	        return;
	    }

	    System.out.println("Select delivery address:");
	    for (int i = 0; i < addresses.size(); i++) {
	        Address a = addresses.get(i);
	        System.out.println((i+1) + ". " + a.getStreet() + ", " + a.getCity() + ", " + a.getState() + " - " + a.getZipcode() + " (" + a.getAddress_type() + ")");
	    }

	    int choice = InputValidate.ChoiceValidation(s, 1, addresses.size());
	    Address selected = addresses.get(choice-1);

	    System.out.println("Order will be delivered to: " + selected.getStreet() + ", " + selected.getCity());
	    System.out.println("Total Amount: ₹" + total);
	    System.out.println();
	    System.out.print("Confirm checkout? (y/n): ");

	    String input = s.nextLine().trim();

	    if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
	    	
	    	System.out.println("Do you have a promo code? (y/n): ");
	    	String promoInput = s.nextLine().trim();
	    	if(promoInput.equalsIgnoreCase("y")) {
	    		String promoCode = InputValidate.DescURLValidation(s, "Enter promo code: ");
	    		try {
	    			total = CustomerDAO.applyDiscount(promoCode, total);
	    			System.out.println("Discount applied! New total : ₹"+total);
	    		}catch(DBAccessException e) {
	    			System.out.println(e.getMessage());
	    		}
	    	}
	    	
	    	
	    	
	    	System.out.println("Select payment method: 1.COD 2.Card 3.UPI");
	    	int payChoice = InputValidate.ChoiceValidation(s,1,3);
	    	String method = "";
	    	if(payChoice == 1) {
	    		method = "cod";
	    	}
	    	else if(payChoice == 2) {
	    		method = "card";
	    	}
	    	else if(payChoice == 3) {
	    		method = "upi";
	    	}
	    	String txnId = "TXN" + System.currentTimeMillis();
	    	int orderId = CustomerDAO.createOrder(customer.getEmail(), items, total, selected.getAddress_id());
	        CustomerDAO.createPayment(orderId, method, "success", txnId);
	        System.out.println("Payment Successful");
	        System.out.println("Order placed successfully!");
	        System.out.println();
	    } else {
	        System.out.println("Checkout cancelled.");
	        System.out.println();
	    }
	}

	
	public static void viewOrders(Customer customer) {
	    try {
	        List<Order> orders = CustomerDAO.getOrders(customer.getEmail());

	        if (orders.isEmpty()) {
	            System.out.println("No orders found.");
	            return;
	        }

	        for (Order order : orders) {
	            order.displayOrder();
	        }

	    } catch (DBAccessException e) {
	        System.out.println(e.getMessage());
	    }
	}

	
	public static void profileMenu(Scanner s,Customer customer) throws DBAccessException, EmptyInputException, InputMismatchException{
		int choice;
		do {
			System.out.println("1.View Profile\n2.Edit Name\n3.Edit email\n4.Edit Password\n5.Manage Addresses\n6.Back");
			choice = InputValidate.ChoiceValidation(s, 1, 6);
			switch(choice) {
			case 1:
				System.out.println("Name: "+customer.getName() +"\nEmail: "+customer.getEmail());
				break;
			case 2:
				String newName = InputValidate.NameValidation(s, "Enter new Name: ");
				UserDAO.updateUserName(UserDAO.getUserIdByEmail(customer.getEmail()),newName);
				customer = new Customer(newName,customer.getEmail(),customer.getPassword(),customer.getStatus());
				System.out.println("Name updated successfully.");
				break;
				
			case 3:
				String newEmail = InputValidate.EmailValidation(s, "Enter new Email: ");
				UserDAO.updateUserEmail(UserDAO.getUserIdByEmail(customer.getEmail()),newEmail);
				customer = new Customer(customer.getName(),newEmail,customer.getPassword(),customer.getStatus());
				System.out.println("Email update successfully.");
				break;
				
				
			case 4:
				String newPassword = InputValidate.PasswordValidation(s, "Enter new Password: ");
				UserDAO.updateUserPassword(UserDAO.getUserIdByEmail(customer.getEmail()),newPassword);
				System.out.println("Password updated successfully.");
				break;
				
			case 5:
				manageAddresses(s,customer);
				break;
				
			case 6:
				return;
			}
		}while(choice != 6);
	}


	private static void manageAddresses(Scanner s, Customer customer) throws DBAccessException, EmptyInputException, InputMismatchException{
		int choice;
		try {
			int userId = UserDAO.getUserIdByEmail(customer.getEmail());
		do {
			System.out.println("1.View all addresses\n2.Add new Address\n3.Update existing Address\n4.Delete existing Address\n5.Back");
			choice = InputValidate.ChoiceValidation(s, 1, 5);
			switch(choice) {
			case 1:
				List<Address> addresses = UserDAO.getAddressesByUserId(userId); 
				if (addresses.isEmpty()) { System.out.println("No addresses found."); 
				} 
				else { 
					for (Address a : addresses) { 
						System.out.println("ID: " + a.getAddress_id() + " | " + a.getStreet() + ", " + a.getCity() + ", " + a.getState() + " - " + a.getZipcode() + " (" + a.getAddress_type() + ")"); 
					} 
				} 
				System.out.println(); 
				break;
				
			case 2:
				String street = InputValidate.AddressValidation(s, "Enter street: "); 
				String city = InputValidate.AddressValidation(s, "Enter city: "); 
				String state = InputValidate.AddressValidation(s, "Enter state: "); 
				String zipcode = InputValidate.PincodeValidation(s, "Enter pincode: "); 
				String type = InputValidate.AddressTypeValidation(s, "Enter type (home/office/other): "); 
				UserDAO.addAddress(userId, street, city, state, zipcode, type); 
				System.out.println("Address added successfully.\n"); 
				break;
				
			case 3:
				addresses = UserDAO.getAddressesByUserId(userId); 
				if (addresses.isEmpty()) { 
					System.out.println("No addresses to edit.\n"); 
					break; 
				} 
				System.out.println("Select address ID to edit:"); 
				for (Address a : addresses) { 
					System.out.println(a.getAddress_id() + ": " + a.getStreet() + ", " + a.getCity()); 
				} 
				int addrId = InputValidate.IntValidation(s, "Enter address ID: "); 
				street = InputValidate.AddressValidation(s, "Enter new street: "); 
				city = InputValidate.AddressValidation(s, "Enter new city: "); 
				state = InputValidate.AddressValidation(s, "Enter new state: "); 
				zipcode = InputValidate.PincodeValidation(s, "Enter new pincode: "); 
				type = InputValidate.AddressTypeValidation(s, "Enter new type (home/office/other): "); 
				UserDAO.updateAddress(addrId,userId, street, city, state, zipcode, type); 
				System.out.println("Address updated successfully.\n"); 
				break;
				
			case 4:
				addresses = UserDAO.getAddressesByUserId(userId); 
				if (addresses.isEmpty()) { 
					System.out.println("No addresses to delete.\n"); 
					break; 
				} 
				System.out.println("Select address ID to delete:"); 
				for (Address a : addresses) { 
					System.out.println(a.getAddress_id() + ": " + a.getStreet() + ", " + a.getCity()); 
				} 
				addrId = InputValidate.IntValidation(s, "Enter address ID: "); 
				UserDAO.deleteAddress(addrId,userId); 
				System.out.println("Address deleted successfully.\n"); 
				break;
				
			case 5:
				return;
			}
		}while(choice != 5);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public static void viewDiscounts() throws DBAccessException{
		List<Discount> discounts = CustomerDAO.getActiveDiscounts();
		if(discounts.isEmpty()) {
			System.out.println("No discounts available.");
			return;
		}
		for(Discount d : discounts) {
			System.out.println("Code: " +d.getCode()+ " | "+d.getDPT()+" % off | Expires: "+d.getExpDate());
		}
	}

	public static void submitSupportTicket(Scanner s, Customer customer) {
	    try {
	        int userId = com.ecommerce.dao.UserDAO.getUserIdByEmail(customer.getEmail());

	        System.out.println("Select issue type: payment / delivery / refund / return / other");
	        String issueType = InputValidate.StringValidation(s, "Enter issue type: ").toLowerCase();
	        while (!issueType.matches("payment|delivery|refund|return|other")) {
	            System.out.println("Invalid type. Choose: payment / delivery / refund / return / other");
	            issueType = InputValidate.StringValidation(s, "Enter issue type: ").toLowerCase();
	        }

	        String description = InputValidate.DescURLValidation(s, "Enter issue description: ");

	        System.out.println("Do you want to link this ticket to an order? (y/n): ");
	        String choice = s.nextLine().trim();
	        Integer orderId = null;
	        if (choice.equalsIgnoreCase("y")) {
	            orderId = InputValidate.IntValidation(s, "Enter order ID: ");
	        }

	        CustomerDAO.createSupportTicket(userId, orderId, issueType, description);
	        System.out.println("Support ticket submitted successfully!");
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}


	public static void cancelOrder(Scanner s, Customer customer) {
	    try {
	        int userId = UserDAO.getUserIdByEmail(customer.getEmail());

	        List<Order> orders = CustomerDAO.getOrders(customer.getEmail());

	        if (orders.isEmpty()) {
	            System.out.println("You have no orders to cancel.");
	            return;
	        }

	        System.out.println("===== Your Orders =====");
	        for (Order order : orders) {
	            order.displayOrder();
	        }

	        int orderId = InputValidate.IntValidation(s, "Enter Order ID to cancel: ");

	        System.out.print("Are you sure you want to cancel Order #" + orderId + "? (y/n): ");
	        String confirm = s.nextLine().trim();

	        if (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("yes")) {
	            System.out.println("Cancellation aborted.");
	            return;
	        }

	        CustomerDAO.cancelOrder(orderId, userId);

	        System.out.println("Order #" + orderId + " has been cancelled successfully.");
	        System.out.println("Your payment has been marked as refunded.");
	        System.out.println();

	    } catch (EntityNotFoundException | DBAccessException e) {
	        System.out.println(e.getMessage());
	    } catch (EmptyInputException | InputMismatchException e) {
	        System.out.println(e.getMessage());
	    }
	}
}
