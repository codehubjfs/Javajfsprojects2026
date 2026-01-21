package service;

import dao.BookingDAO;
import dao.RoomDAO;

import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Scanner;

public class BookingService {

    public static void bookingMenuGuset(Scanner scan, int userId) {

        while (true) {
            System.out.println("\n--- BOOKING MENU ---");
            System.out.println("1. Create Booking");
            System.out.println("2. View My Bookings");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Back");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	createBooking(scan, userId);
                	break;
                case 2:
                	viewBookings(userId);
                	break;
                case 3:
                	cancelBooking(scan, userId);
                	break;
                case 4:
                	return;
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }

    public static void bookingMenuReceptionist(Scanner scan, int userId) {

    	int guestId;
    	
        while (true) {
            System.out.println("\n--- BOOKING MENU ---");
            System.out.println("1. Create Booking");
            System.out.println("2. View Bookings");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Check-In");
            System.out.println("5. Check-Out");
            System.out.println("6. Back");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	System.out.print("\nEnter Guest ID: ");
                    guestId = scan.nextInt();
                    scan.nextLine();
                	createBooking(scan, guestId);
                	break;
                case 2:
                	viewAllBookings();
                	break;
                case 3:
                	System.out.print("\nEnter Guest ID: ");
                    guestId = scan.nextInt();
                    scan.nextLine();
                	cancelBooking(scan, guestId);
                	break;
                case 4:
                	checkIn(scan);
                	break;
                case 5:
                	checkOut(scan);
                	break;
                case 6:
                	return;
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }
    
    public static void createBooking(Scanner scan, int userId) {
        try {
            RoomDAO roomDAO = new RoomDAO();
            ResultSet rs = roomDAO.getAvailableRooms();

            System.out.println("\nAVAILABLE ROOMS:");
            System.out.printf("%-8s %-12s %-10s %-15s%n", "RoomId", "RoomType", "MaxGuest", "PricePerNight");

            while (rs.next()) {
                System.out.printf("%-8d %-12s %-10d $%-14.2f%n",
                    rs.getInt("room_id"),
                    rs.getString("room_type"),
                    rs.getInt("maximum_guest"),
                    rs.getDouble("price_per_night")
                );
            }

            System.out.print("\nEnter Room ID: ");
            int roomId = scan.nextInt();
            scan.nextLine();

            System.out.print("Check-In date (yyyy-mm-dd): ");
            LocalDate checkIn = LocalDate.parse(scan.nextLine());

            System.out.print("Check-Out date (yyyy-mm-dd): ");
            LocalDate checkOut = LocalDate.parse(scan.nextLine());

            BookingDAO bookingDAO = new BookingDAO();
            double price = roomDAO.getRoomPrice(roomId);

            Duration duration = Duration.between(checkIn.atStartOfDay(), checkOut.atStartOfDay());
            long days = duration.toDays();
            double total = price * days;

            int bookingId = bookingDAO.createBooking( userId, roomId, checkIn, checkOut, total);

            roomDAO.updateRoomStatus(roomId, "BOOKED");
            
            System.out.println("Booking Id: " + bookingId);
            System.out.println("Booking created! Total: $" + total);

        } catch (Exception e) {
            System.out.println("Booking Error: " + e.getMessage());
        }
    }

    public static void viewBookings(int userId) {
        try {
            BookingDAO dao = new BookingDAO();
            ResultSet rs = dao.getBookingsByUser(userId);

            System.out.println("\nYOUR BOOKINGS:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("booking_id") + " | " +
                    rs.getString("room_id") + " | " +
                    rs.getString("booking_status") + " | $" +
                    rs.getDouble("total_amount")
                );
            }

        } catch (Exception e) {
            System.out.println("View Error: " + e.getMessage());
        }
    }

    public static void cancelBooking(Scanner scan, int userId) {
        try {
            System.out.print("Enter Booking ID: ");
            int bookingId = scan.nextInt();
                      
            BookingDAO bookingDAO = new BookingDAO();
            
            System.out.print("Enter Room ID: ");
            int roomId = scan.nextInt();
            
            bookingDAO.cancelBooking(bookingId, roomId);

            System.out.println("Booking cancelled!");

        } catch (Exception e) {
            System.out.println("Cancel Error: " + e.getMessage());
        }
    }

    public static void checkIn(Scanner scan) {
        try {
            System.out.print("Enter Booking ID: ");
            int id = scan.nextInt();

            BookingDAO bookingDAO = new BookingDAO();
            bookingDAO.checkIn(id);

            System.out.println("Guest Checked-In!");

        } catch (Exception e) {
            System.out.println("Check-In Error: " + e.getMessage());
        }
    }

    public static void checkOut(Scanner scan) {
        try {
            System.out.print("Enter Booking ID: ");
            int id = scan.nextInt();

            BookingDAO bookingDAO = new BookingDAO();
            bookingDAO.checkOut(id);

            ResultSet rs = bookingDAO.getRoomIdByBookingId(id);
            RoomDAO roomDAO = new RoomDAO();
            while (rs.next()) {
                roomDAO.updateRoomStatus(rs.getInt("room_id"), "AVAILABLE");
            }
          
            System.out.println("Checked-Out!.");
            
            

        } catch (Exception e) {
            System.out.println("Check-Out Error: " + e.getMessage());
        }
    }
    
    public static void viewAllBookings() {
    	try {
	    	BookingDAO dao = new BookingDAO();
	        ResultSet rs = dao.getBookings();
	
	        System.out.println("\nBOOKING STATUS:");
	        while (rs.next()) {
	            System.out.println(
	                rs.getInt("booking_id") + " | " +
	                rs.getString("room_id") + " | " +
	                rs.getString("booking_status") + " | $" +
	                rs.getDouble("total_amount")
	            );
	        }
	    } catch (Exception e) {
	        System.out.println("View Error: " + e.getMessage());
	    }
    }
}
