package service;

import dao.ServiceDAO;
import dao.ServiceRequestDAO;
import dao.BookingDAO;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ServiceForRoom {

	public static void serviceMenuAdmin(Scanner scan) {

	    while (true) {
	        System.out.println("\n--- ADMIN SERVICE MENU ---");
	        System.out.println("1. View All Services");
	        System.out.println("2. Add New Service");
	        System.out.println("3. Update Service");
	        System.out.println("4. Delete Service");
	        System.out.println("5. Back");
	        System.out.print("Choice: ");

	        int choice = scan.nextInt();
	        scan.nextLine();

	        switch (choice) {
	            case 1:
	                viewServices();
	                break;
	            case 2:
	                addService(scan);
	                break;
	            case 3:
	                updateService(scan);
	                break;
	            case 4:
	                deleteService(scan);
	                break;
	            case 5:
	                return;
	            default:
	                System.out.println("Invalid choice! Please try again.");
	        }
	    }
	}

	public static void serviceMenuGuest(Scanner scan, int userId) {

	    while (true) {
	        System.out.println("\n--- GUEST SERVICE MENU ---");
	        System.out.println("1. View Available Services");
	        System.out.println("2. Request Service");
	        System.out.println("3. View My Service Requests");
	        System.out.println("4. Back");
	        System.out.print("Choice: ");

	        int choice = scan.nextInt();
	        scan.nextLine();

	        switch (choice) {
	            case 1:
	                viewServices();
	                break;
	            case 2:
	                requestService(scan, userId);
	                break;
	            case 3:
	                viewServiceRequests(scan, userId);
	                break;
	            case 4:
	                return;
	            default:
	                System.out.println("Invalid choice!");
	        }
	    }
	}


	public static void serviceMenuReceptionist(Scanner scan, int userId) {

	    while (true) {
	        System.out.println("\n--- RECEPTIONIST SERVICE MENU ---");
	        System.out.println("1. View Available Services");
	        System.out.println("2. View All Service Requests");
	        System.out.println("3. Update Service Status");
	        System.out.println("4. Back");
	        System.out.print("Choice: ");

	        int choice = scan.nextInt();
	        scan.nextLine();

	        switch (choice) {
	            case 1:
	                viewServices();
	                break;
	            case 2:
	                viewAllServiceRequests(); 
	                break;
	            case 3:
	                updateServiceStatus(scan);
	                break;
	            case 4:
	                return;
	            default:
	                System.out.println("Invalid choice!");
	        }
	    }
	}

    
    public static void viewServices() {
        try {
            ServiceDAO dao = new ServiceDAO();
            ResultSet rs = dao.getAllServices();

            System.out.println("\n--- AVAILABLE SERVICES ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("service_id") + " | " +
                    rs.getString("service_name") + " | $" +
                    rs.getDouble("service_price")
                );
            }

            if (!found) System.out.println("No services available.");

        } catch (Exception e) {
            System.out.println("Service View Error: " + e.getMessage());
        }
    }

    public static void requestService(Scanner scan, int userId) {
        try {
            BookingDAO bookingDAO = new BookingDAO();

            System.out.println("\n--- YOUR ACTIVE BOOKINGS ---");
            ResultSet rs = bookingDAO.getActiveBookings(userId);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("booking_id") + " | Room: " +
                    rs.getInt("room_id") + " | Status: " +
                    rs.getString("booking_status")
                );
            }

            if (!found) {
                System.out.println("No active booking to request services.");
                return;
            }

            System.out.print("Enter Booking ID: ");
            int bookingId = scan.nextInt();
            scan.nextLine();

            viewServices();
            System.out.print("Enter Service ID: ");
            int serviceId = scan.nextInt();
            scan.nextLine();

            ServiceRequestDAO requestDAO = new ServiceRequestDAO();
            requestDAO.createRequest(LocalDateTime.now(), "REQUESTED", bookingId, serviceId);

            System.out.println("Service requested successfully!");

        } catch (Exception e) {
            System.out.println("Service Request Error: " + e.getMessage());
        }
    }

    public static void updateServiceStatus(Scanner scan) {
        try {
            System.out.print("Enter Request ID: ");
            int requestId = scan.nextInt();
            scan.nextLine();

            System.out.println("Select Status:");
            System.out.println("1. IN_PROGRESS");
            System.out.println("2. COMPLETED");
            System.out.println("3. CANCELLED");
            System.out.print("Choice: ");
            int choice = scan.nextInt();
            scan.nextLine();

            String status;
            switch (choice) {
                case 1: 
                	status = "IN_PROGRESS";
                	break;
                case 2:
                	status = "COMPLETED";
                	break;
                case 3: 
                	status = "CANCELLED";
                	break;
                default: 
                	status = "REQUESTED";
            };

            ServiceRequestDAO requestDAO = new ServiceRequestDAO();
            requestDAO.updateStatus(requestId, status);

            System.out.println("Service request updated!");

        } catch (Exception e) {
            System.out.println("Update Service Error: " + e.getMessage());
        }
    }

    public static void viewServiceRequests(Scanner scan, int userId) {
        try {
        	BookingDAO bookingDAO = new BookingDAO();

            System.out.println("\n--- YOUR ACTIVE BOOKINGS ---");
            ResultSet rs = bookingDAO.getActiveBookings(userId);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("booking_id") + " | Room: " +
                    rs.getInt("room_id") + " | Status: " +
                    rs.getString("booking_status")
                );
            }

            if (!found) {
                System.out.println("No active booking to request services.");
                return;
            }

            System.out.print("Enter Booking ID: ");
            int bookingId = scan.nextInt();
            scan.nextLine();
        	
            ServiceRequestDAO dao = new ServiceRequestDAO();
            rs = dao.getServiceRequestsByBookingId(bookingId);

            System.out.println("\n--- SERVICE REQUESTS ---");

           found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    "Req#" + rs.getInt("request_id") +
                    " | Booking: " + rs.getInt("booking_id") +
                    " | Service: " + rs.getString("service_name") +
                    " | Status: " + rs.getString("request_status")
                );
            }

            if (!found) System.out.println("No service requests found.");

        } catch (Exception e) {
            System.out.println("View Service Error: " + e.getMessage());
        }
    }
    
    public static void viewAllServiceRequests() {
        try {
            ServiceRequestDAO dao = new ServiceRequestDAO();
            ResultSet rs = dao.getAllServiceRequests();

            System.out.println("\n--- ALL SERVICE REQUESTS ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    "Req#" + rs.getInt("request_id") +
                    " | Booking: " + rs.getInt("booking_id") +
                    " | Service: " + rs.getString("service_name") +
                    " | Status: " + rs.getString("request_status")
                );
            }

            if (!found) System.out.println("No service requests found.");

        } catch (Exception e) {
            System.out.println("View All Service Error: " + e.getMessage());
        }
    }
    
    public static void addService(Scanner scan) {
        try {
            System.out.print("Enter Service Name: ");
            String name = scan.nextLine();

            System.out.print("Enter Service Price: ");
            double price = scan.nextDouble();
            scan.nextLine();

            ServiceDAO dao = new ServiceDAO();
            dao.addService(name, price);

            System.out.println("Service added successfully!");

        } catch (Exception e) {
            System.out.println("Add Service Error: " + e.getMessage());
        }
    }

    public static void updateService(Scanner scan) {
        try {
            viewServices();

            System.out.print("Enter Service ID to update: ");
            int id = scan.nextInt();
            scan.nextLine();

            System.out.print("Enter new Service Name: ");
            String name = scan.nextLine();

            System.out.print("Enter new Price: ");
            double price = scan.nextDouble();
            scan.nextLine();

            ServiceDAO dao = new ServiceDAO();
            dao.updateService(id, name, price);

            System.out.println("Service updated successfully!");

        } catch (Exception e) {
            System.out.println("Update Service Error: " + e.getMessage());
        }
    }

    public static void deleteService(Scanner scan) {
        try {
            viewServices();

            System.out.print("Enter Service ID to delete: ");
            int id = scan.nextInt();
            scan.nextLine();

            ServiceDAO dao = new ServiceDAO();
            dao.deleteService(id);

            System.out.println("Service deleted successfully!");

        } catch (Exception e) {
            System.out.println("Delete Service Error: " + e.getMessage());
        }
    }


}
