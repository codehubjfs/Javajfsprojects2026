package app;

import java.util.Scanner;

import service.*;

/**
 * This class manage the Hotel Management and Booking System.
 * @author KavinT
 * @since 1.0 
 */
public class MainApp {

	/**
	 * This is main method will direct to login or register page.
	 * @param args
	 */
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("\n-------- HOTEL BOOKING SYSTEM --------");
            System.out.println("1. Register or Login");
            System.out.println("2. Exit");
            System.out.print("Choice: ");

            int choice = scan.nextInt();

            switch (choice) {
                case 1:
                	AuthService.registerOrLogin(scan);
                    break;
                case 2:
                    System.out.println("Thank you!");
                    scan.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
