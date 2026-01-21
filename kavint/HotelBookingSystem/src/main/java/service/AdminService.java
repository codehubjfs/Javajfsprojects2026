package service;

import java.util.Scanner;

public class AdminService {


    public static void adminAccess(Scanner scan, int userId) {

        try {
            // ---------------- ADMIN MENU LOOP ----------------
        	int choice;

        	while (true) {
        	    System.out.println("\n--- ADMIN MENU ---");
        	    System.out.println("1. View Booking Status");
        	    System.out.println("2. Manage Rooms");
        	    System.out.println("3. Manage Amenities");
        	    System.out.println("4. Manage Services");
        	    System.out.println("5. Manage Guests");
        	    System.out.println("6. Manage Staff");
        	    System.out.println("7. View Reports");
        	    System.out.println("8. Logout");
        	    System.out.print("Choice: ");

        	    choice = scan.nextInt();
        	    scan.nextLine();

        	    switch (choice) {

        	        case 1:
        	            BookingService.viewAllBookings();;
        	            break;

        	        case 2:
        	            RoomService.roomMenu(scan);
        	            break;

        	        case 3:
        	            AmenityService.amenityMenu(scan);
        	            break;

        	        case 4:
        	            ServiceForRoom.serviceMenuAdmin(scan);
        	            break;

        	        case 5:
        	            System.out.println("Managing guests... (Feature Deployment)");
        	            break;

        	        case 6:
        	            System.out.println("Managing staff... (Feature Deployment)");
        	            break;

        	        case 7:
        	            System.out.println("Generating reports... (Feature Deployment)");
        	            break;

        	        case 8:
        	            System.out.println("Admin logged out.");
        	            return;

        	        default:
        	            System.out.println("Invalid choice! Please try again.");
        	    }
        	}

        } catch (Exception e) {
            System.out.println("Admin error: " + e.getMessage());
        }
    }
}
