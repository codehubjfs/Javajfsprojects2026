package service;

import java.util.Scanner;

public class ReceptionistService {

    public static void receptionistAccess(Scanner scan, int userId) {

        try {
        	int choice;
            while (true) {
                System.out.println("\n--- RECEPTIONIST MENU ---");
                System.out.println("1. Register Guest");
                System.out.println("2. Booking Service");
                System.out.println("3. Room Service (Manage) ");
                System.out.println("4. Logout");
                System.out.print("Choice: ");

                choice = scan.nextInt();
                scan.nextLine();

                if (choice == 1) {
                	AuthService.registerGuest(scan);
                }
                else if (choice == 2) {
                	BookingService.bookingMenuReceptionist(scan, userId);
                }
                else if (choice == 3) {
                	ServiceForRoom.serviceMenuReceptionist(scan, userId);
                }
                if (choice == 4) {
                    System.out.println("Receptionist logged out.");
                    return;
                }
                else {
                	System.out.println("Invalid choice!.");
                }
               
            }

        } catch (Exception e) {
            System.out.println("Receptionist error: " + e.getMessage());
        }
    }
}
