package service;

import dao.BookingDAO;
import dao.PaymentDAO;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Scanner;

public class PaymentService {

    public static void paymentMenu(Scanner scan, int userId) {

        while (true) {
            System.out.println("\n--- PAYMENT MENU ---");
            System.out.println("1. Make Payment");
            System.out.println("2. View My Payments");
            System.out.println("3. Back");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	makePayment(scan, userId);
                	break;
                case 2:
                	viewPayments(userId);
                	break;
                case 3:
                	return;
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }

    public static void makePayment(Scanner scan, int userId) {
        try {
            BookingDAO bookingDAO = new BookingDAO();
            PaymentDAO paymentDAO = new PaymentDAO();

            System.out.println("\n--- YOUR BOOKING PENDING PAYMENT ---");
            ResultSet rs = bookingDAO.getPendingPayments(userId);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("booking_id") + " | Amount: $" +
                        rs.getDouble("total_amount") + " | Status: " +
                        rs.getString("booking_status")
                );
            }

            if (!found) {
                System.out.println("No pending payments!");
                return;
            }

            System.out.print("\nEnter Booking ID to pay: ");
            int bookingId = scan.nextInt();
            scan.nextLine();

            double amount = bookingDAO.getBookingAmount(bookingId);

            System.out.println("\nSelect Payment Method:");
            System.out.println("1. UPI");
            System.out.println("2. CARD");
            System.out.println("3. NET_BANKING");
            System.out.println("4. CASH");
            System.out.print("Choice: ");
            int mode = scan.nextInt();
            scan.nextLine();

            String method;
            switch (mode) {
                case 1:
                	method = "UPI";
                	break;
                case 2:
                	method = "CARD";
                	break;
                case 3:
                	method = "NET_BANKING";
                	break;
                case 4:
                	method = "CASH";
                	break;
                default:
                	method = "CASH";
                	break;
            };

            // Record payment
            paymentDAO.createPayment(
                    LocalDateTime.now(),
                    method,
                    "SUCCESS",
                    amount,
                    bookingId
            );

            System.out.println("Payment of $" + amount + " successful via " + method);

        } catch (Exception e) {
            System.out.println("Payment Error: " + e.getMessage());
        }
    }

    public static void viewPayments(int userId) {
        try {
            PaymentDAO dao = new PaymentDAO();
            ResultSet rs = dao.getPaymentsByUser(userId);

            System.out.println("\n--- PAYMENT HISTORY ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("payment_id") + " | Booking: " +
                    rs.getInt("booking_id") + " | $" +
                    rs.getDouble("amount") + " | " +
                    rs.getString("payment_method") + " | " +
                    rs.getString("payment_status")
                );
            }

            if (!found) System.out.println("No payments found.");

        } catch (Exception e) {
            System.out.println("View Payment Error: " + e.getMessage());
        }
    }
}
