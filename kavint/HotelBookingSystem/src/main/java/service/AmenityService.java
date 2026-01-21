package service;

import dao.AmenityDAO;
import dao.RoomAmenityDAO;

import java.sql.ResultSet;
import java.util.Scanner;

public class AmenityService {

    public static void amenityMenu(Scanner scan) {

        while (true) {
            System.out.println("\n--- AMENITY MENU ---");
            System.out.println("1. View Amenities");
            System.out.println("2. Add Amenity (Admin)");
            System.out.println("3. Assign Amenity to Room (Admin)");
            System.out.println("4. View Amenities of a Room");
            System.out.println("5. Back");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	viewAmenities();
                	break;
                case 2:
                	addAmenity(scan);
                	break;
                case 3:
                	assignAmenityToRoom(scan);
                	break;
                case 4:
                	viewRoomAmenities(scan);
                	break;
                case 5:
                	return;
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }

    public static void viewAmenities() {
        try {
            AmenityDAO dao = new AmenityDAO();
            ResultSet rs = dao.getAllAmenities();

            System.out.println("\n--- AMENITIES ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("amenity_id") + " | " +
                    rs.getString("amenity_name")
                );
            }

            if (!found) System.out.println("No amenities found.");

        } catch (Exception e) {
            System.out.println("View Amenity Error: " + e.getMessage());
        }
    }

    public static void addAmenity(Scanner scan) {
        try {
            System.out.print("Enter Amenity Name: ");
            String name = scan.nextLine();

            AmenityDAO dao = new AmenityDAO();
            dao.addAmenity(name);

            System.out.println("Amenity added successfully!");

        } catch (Exception e) {
            System.out.println("Add Amenity Error: " + e.getMessage());
        }
    }

    public static void assignAmenityToRoom(Scanner scan) {
        try {
            viewAmenities();

            System.out.print("Enter Room ID: ");
            int roomId = scan.nextInt();
            scan.nextLine();

            System.out.print("Enter Amenity ID: ");
            int amenityId = scan.nextInt();
            scan.nextLine();

            RoomAmenityDAO dao = new RoomAmenityDAO();
            dao.assignAmenity(roomId, amenityId);

            System.out.println("Amenity assigned to room successfully!");

        } catch (Exception e) {
            System.out.println("Assign Amenity Error: " + e.getMessage());
        }
    }

    public static void viewRoomAmenities(Scanner scan) {
        try {
            System.out.print("Enter Room ID: ");
            int roomId = scan.nextInt();
            scan.nextLine();

            RoomAmenityDAO dao = new RoomAmenityDAO();
            ResultSet rs = dao.getAmenitiesByRoom(roomId);

            System.out.println("\n--- ROOM AMENITIES ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getString("amenity_name")
                );
            }

            if (!found) System.out.println("No amenities assigned.");

        } catch (Exception e) {
            System.out.println("Room Amenity Error: " + e.getMessage());
        }
    }
}
