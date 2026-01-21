package service;

//import dao.GuestDAO;
//import dao.LocationDAO;
//import model.Guest;
//import model.Location;
//
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;

public class GuestService {

//    private static List<Guest> guestList = new ArrayList<>();

    public static void guestAccess(Scanner scan, int userId) {
        try {
//            GuestDAO dao = new GuestDAO();
//            ResultSet rs = dao.getGuestByUserId(userId);
//
//            if (!rs.next()) {
//                System.out.println("Guest not found!");
//                return;
//            }
//
//            // load location
//            int locationId = rs.getInt("location_id");
//            Location location = null;
//            if (locationId > 0) {
//                LocationDAO locationDAO = new LocationDAO();
//                location = locationDAO.getLocationById(locationId);
//            }
//
//            Guest guest = new Guest(
//                    rs.getInt("user_id"),
//                    rs.getString("name"),
//                    rs.getString("email"),
//                    rs.getString("phone"),
//                    rs.getString("gender"),
//                    location,
//                    rs.getString("id_proof"),
//                    rs.getString("created_at"),
//                    rs.getString("last_login"),
//                    rs.getString("status"),
//                    rs.getInt("guest_id")
//            );
//
//            guestList.add(guest);

            int choice;
            while (true) {
                System.out.println("\n--- GUEST MENU ---");
                System.out.println("1. Booking Services");
                System.out.println("2. Service Reguest");
                System.out.println("3. Logout");
                System.out.print("Choice: ");

                choice = scan.nextInt();

                if (choice == 1) {
                	BookingService.bookingMenuGuset(scan, userId);
                }
                else if (choice == 2) {
                	ServiceForRoom.serviceMenuGuest(scan, userId);
                }
                else if (choice == 2) {
                    System.out.println("Guest logged out.");
                    return;
                }

                System.out.println("Feature under development.");
            }

        } catch (Exception e) {
            System.out.println("Guest error: " + e.getMessage());
        }
    }
}
