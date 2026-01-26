package ServiceLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import DAO.CustomerDAO;
import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
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
	                    double max = InputValidate.DoubleValidation(
	                            s, "Enter max price:", min, Double.MAX_VALUE);
	                    shownProducts = products.stream()
	                            .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
	                            .collect(Collectors.toList());
	                    break;

	                case 5:
	                    System.out.println("1. Ascending\n2. Descending");
	                    int sortChoice = InputValidate.ChoiceValidation(s, 1, 2);
	                    shownProducts = products.stream()
	                            .sorted(sortChoice == 1
	                                    ? Comparator.comparing(Product::getPrice)
	                                    : Comparator.comparing(Product::getPrice).reversed())
	                            .collect(Collectors.toList());
	                    break;

	                case 6:
	                    return;
	            }

	            // 🔥 DISPLAY PRODUCTS
	            display(shownProducts);

	            // 🔥 ADD TO CART
	            if (!shownProducts.isEmpty()) {
	                System.out.print("Add a product to cart? (y/n): ");
	                String input = s.nextLine();
	                char ch = input.charAt(0);


	                if (ch == 'y' || ch == 'Y') {
	                	int productId = InputValidate.IntValidation(s, "Enter Product ID:");
	                	int qty = InputValidate.IntValidation(s, "Enter Quantity:");


	                    addToCart(customer, productId, qty);
	                    System.out.println("✅ Product added to cart.\n");
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
                p.getProductID() + " | " +
                p.getName() + " | " +
                p.getBrand() + " | ₹" +
                p.getPrice()
        ));
        System.out.println();
    }

    
    
    

	public static void addToCart(Customer customer, int productId, int quantity)
	        throws DBAccessException {

	    Product product = CustomerDAO.getProductById(productId);

	    if (product == null) {
	        throw new DBAccessException("Product not found.");
	    }

	    int stock = CustomerDAO.getStock(productId);
	    if (quantity > stock) {
	        throw new DBAccessException("Only " + stock + " items available.");
	    }

	    int cartId = CustomerDAO.getOrCreateCart(customer.getEmail());
	    CustomerDAO.addOrUpdateCartItem(cartId, product, quantity);
	}

    
    
    
    
    
	public static void viewCart(Customer customer) throws DBAccessException {

	    int cartId = CustomerDAO.getOrCreateCart(customer.getEmail());
	    ArrayList<CartItem> items = CustomerDAO.viewCart(cartId);

	    if (items.isEmpty()) {
	        System.out.println("Cart is empty.");
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
	}


	
	
	public static void checkout(Scanner s, Customer customer) throws DBAccessException {

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

	    System.out.println("Total Amount: ₹" + total);
	    System.out.print("Confirm checkout? (y/n): ");

	    String input = s.nextLine().trim();

	    if (input.equalsIgnoreCase("y")) {
	        placeOrder(customer, items, total);
	        System.out.println("✅ Order placed successfully!");
	    } else {
	        System.out.println("Checkout cancelled.");
	    }
	}


	
	
	public static void placeOrder(Customer customer,
            ArrayList<CartItem> items,
            double total)
throws DBAccessException {

CustomerDAO.createOrder(customer.getEmail(), items, total);
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



}
