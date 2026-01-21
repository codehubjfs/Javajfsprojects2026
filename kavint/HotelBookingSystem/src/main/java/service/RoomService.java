package service;

import dao.RoomDAO;
//import model.Room;

import java.sql.ResultSet;
import java.util.Scanner;

public class RoomService {

    public static void roomMenu(Scanner scan) {

        while (true) {
            System.out.println("\n--- ROOM MANAGEMENT ---");
            System.out.println("1. Add Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. Search by Room Type");
            System.out.println("4. Filter by Price Range");
            System.out.println("5. Delete Room");
            System.out.println("6. Back");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	addRoom(scan);
                	break;
                case 2:
                	viewRooms();
                	break;
                case 3:
//                	searchByRoomType(scan);
                	System.out.println("This module will updated as soon as possible!.");
                	break;
                case 4:
                	filterByPrice(scan);
                	break;
                case 5:
                	deleteRoom(scan);
                	break;
                case 6: 
                	return; 	
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }

    public static void addRoom(Scanner scan) {
        try {
            System.out.println("\n--- ADD ROOM ---");

            System.out.print("Room Number: ");
            String roomNo = scan.nextLine();

            System.out.print("Room Type: ");
            String type = scan.nextLine();
            
            System.out.print("Maximum Guest can be allowed: ");
            int maximumGuest = scan.nextInt();
            scan.nextLine();

            System.out.print("Price Per Night: ");
            double price = scan.nextDouble();
            scan.nextLine();

            System.out.print("Description: ");
            String desc = scan.nextLine();

            RoomDAO dao = new RoomDAO();
            dao.addRoom(roomNo, type, maximumGuest, price, desc);

            System.out.println("Room added successfully!");

        } catch (Exception e) {
            System.out.println("Room Add Error: " + e.getMessage());
        }
    }

    public static void viewRooms() {
        try {
            RoomDAO dao = new RoomDAO();
            ResultSet rs = dao.getAllRooms();

            System.out.println("\nROOMS AVAILABLE:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("room_id") + " | " +
                    rs.getString("room_number") + " | " +
                    rs.getString("room_type") + " | ₹" +
                    rs.getDouble("price_per_night") + " | " +
                    rs.getString("availability_status")
                );
            }

        } catch (Exception e) {
            System.out.println("View Rooms Error: " + e.getMessage());
        }
    }

//    public static void searchByRoomType(Scanner scan) {
//        try {
//            System.out.print("Enter Room Type: ");
//            String type = scan.nextLine();
//
//            RoomDAO dao = new RoomDAO();
//            ResultSet rs = dao.searchByType(type);
//
//            System.out.println("\nRESULTS:");
//            while (rs.next()) {
//                System.out.println(
//                    rs.getString("room_number") + " | $" +
//                    rs.getDouble("price_per_night")
//                );
//            }
//
//        } catch (Exception e) {
//            System.out.println("Search Error: " + e.getMessage());
//        }
//    }

    public static void filterByPrice(Scanner scan) {
        try {
            System.out.print("Min Price: ");
            double min = scan.nextDouble();
            System.out.print("Max Price: ");
            double max = scan.nextDouble();

            RoomDAO dao = new RoomDAO();
            ResultSet rs = dao.filterByPrice(min, max);

            System.out.println("\nRESULTS:");
            while (rs.next()) {
                System.out.println(
                    rs.getString("room_number") + " | " +
                    rs.getString("room_type") + " | $" +
                    rs.getDouble("price_per_night")
                );
            }

        } catch (Exception e) {
            System.out.println("Filter Error: " + e.getMessage());
        }
    }

    public static void deleteRoom(Scanner scan) {
        try {
            System.out.print("Enter Room ID to delete: ");
            int id = scan.nextInt();

            RoomDAO dao = new RoomDAO();
            dao.deleteRoom(id);

            System.out.println("Room deleted!");

        } catch (Exception e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }
}
