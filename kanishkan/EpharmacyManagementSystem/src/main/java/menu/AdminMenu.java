package menu;


import java.util.Scanner;

import dao.InventoryMangementDao;
import dao.MedicineDao;
import dao.OrderDao;
import dao.UserDao;
import exception.*;
import service.InventoryService;
import service.MedicineService;
import service.*;

public class AdminMenu {

    static Scanner scanner = new Scanner(System.in);

    public static void showAdminMenu() throws Exception{
        int choice;
        do {
            System.out.println("\n*******ADMIN MENU**********");
            System.out.println("1. User Management");
            System.out.println("2. Medicine Management");
            System.out.println("3. Inventory Management");
            System.out.println("4. Order Management");
            System.out.println("5. Payment & Delivery");
            System.out.println("6. Notifications");
            System.out.println("7. Reports");
            System.out.println("-1. Logout");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    userManagementMenu();
                    break;
                case 2:
                    medicineManagementMenu();
                    break;
                case 3:
                    inventoryManagementMenu();
                    break;
                case 4:
                    orderManagementMenu();
                    break;
                case 5:
                    paymentDeliveryMenu();
                    break;
                case 6:
                    notificationMenu();
                    break;
                case 7:
                    reportsMenu();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }

    private static void userManagementMenu() throws InputMismatchException, Exception{
        int choice;
        do {
            System.out.println("\n*****User Management******");
            System.out.println("1. View all users");
            System.out.println("2. Update user details");
            System.out.println("3. Deactivate user");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    UserDao.viewUsers();
                    break;
                case 2:
                	//UserDao.updateUserDetails();
                    break;
                case 3:
                	System.out.println("Enter the id of the user to deactivate: ");
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }

    private static void medicineManagementMenu() throws Exception{
        int choice;
        do {
            System.out.println("\n********* Medicine Management********");
            System.out.println("1. Add new medicine");
            System.out.println("2. Update medicine details");
            System.out.println("3. View all medicines");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:   
                	MedicineService.getMedicineDetails();
                    break;
                case 2:
                    break;
                case 3:
                    MedicineDao.viewAllMedicines();
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }

    private static void inventoryManagementMenu() throws Exception{
        int choice;
        do {
            System.out.println("\n*********Inventory Management********");
            System.out.println("1. Update stock");
            System.out.println("2. View inventory");
            System.out.println("3. View Low stock medicines");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    InventoryService.updatebyUserInput();
                    break;
                case 2:
                	InventoryMangementDao.viewInventory();
                    break;
                case 3:
                    InventoryMangementDao.viewLowStockProducts();
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }

    private static void orderManagementMenu() throws Exception {
        int choice;
        do {
            System.out.println("\n********* Order Management**********");
            System.out.println("1. View all orders");
            System.out.println("2. Update order status");
            System.out.println("3. Cancel order");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                	OrderDao.viewOrders();
                    break;
                case 2:
                	OrderService.updateOrderStatus();
                    break;
                case 3:
                	OrderService.cancelOrder();
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }

    private static void paymentDeliveryMenu() {
        int choice;
        do {
            System.out.println("\n********Payment & Delivery*********");
            System.out.println("1. View payment details");
            System.out.println("2. Track delivery");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }

    private static void notificationMenu() {
        int choice;
        do {
            System.out.println("\n********Notifications********");
            System.out.println("1. Send order updates");
            System.out.println("2. Send promotional notifications");
            System.out.println("3. View stock alerts");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case -1:
                    break;
                default:
            }
        } while (choice != -1);
    }

    private static void reportsMenu() {
        int choice;
        do {
            System.out.println("\n*******Reports********");
            System.out.println("1. Sales report");
            System.out.println("2. Stock report");
            System.out.println("-1. Back");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != -1);
    }
}
