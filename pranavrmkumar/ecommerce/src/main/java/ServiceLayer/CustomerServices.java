package ServiceLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import DAO.CustomerDAO;
import DAO.UserDAO;
import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import Model.Address;
import Model.CartItem;
import Model.Customer;
import Model.Order;
import Model.Product;
import util.InputValidate;

public class CustomerServices {
	
	public static void viewProductsMenu(Scanner s, Customer customer) {

	    try {
	        List<Product> products = CustomerDAO.getActiveProducts();
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

        Product product = CustomerDAO.getProductById(productId);

        if (product == null) {
            System.out.println("Product not Found");
            return;
        }

        int stock = CustomerDAO.getStock(productId);

        while (true) {
            try {
                System.out.print("Enter quantity (Available: " + stock + "): ");
                int quantity = s.nextInt();
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
	    ArrayList<CartItem> items = CustomerDAO.viewCart(cartId);

	    if (items.isEmpty()) {
	        System.out.println("Cart is empty. Cannot checkout.");
	        return;
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
	        placeOrder(customer, items, total,selected.getAddress_id());
	        System.out.println("Order placed successfully!");
	        System.out.println();
	    } else {
	        System.out.println("Checkout cancelled.");
	        System.out.println();
	    }
	}


	
	
	public static void placeOrder(Customer customer,ArrayList<CartItem> items,double total,int addressId)
            		throws DBAccessException {

		CustomerDAO.createOrder(customer.getEmail(), items, total,addressId);
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

	    } catch (DBAccessException | IOException e) {
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
		// TODO Auto-generated method stub
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
				UserDAO.updateAddress(addrId, street, city, state, zipcode, type); 
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
				UserDAO.deleteAddress(addrId); 
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

}
