package service;

import dao.BookingDAO;

import dao.InvoiceDAO;
//import dao.PaymentDAO;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Scanner;

public class InvoiceService {

    public static void invoiceMenu(Scanner scan, int userId) {

        while (true) {
            System.out.println("\n--- INVOICE MENU ---");
            System.out.println("1. Generate Invoice");
            System.out.println("2. View My Invoices");
            System.out.println("3. Back");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	generateInvoice(scan, userId);
                	break;
                case 2:
                	viewInvoices(userId);
                	break;
                case 3:
                	return;
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }

    public static void generateInvoice(Scanner scan, int userId) {
        try {
            BookingDAO bookingDAO = new BookingDAO();
            InvoiceDAO invoiceDAO = new InvoiceDAO();
//            PaymentDAO paymentDAO = new PaymentDAO();

            System.out.println("\n--- BOOKINGS ELIGIBLE FOR INVOICE ---");
            ResultSet rs = bookingDAO.getCompletedBookings(userId);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("booking_id") + " | $" +
                    rs.getDouble("total_amount") + " | CHECKED_OUT"
                );
            }

            if (!found) {
                System.out.println("No invoices can be generated right now!");
                return;
            }

            System.out.print("\nEnter Booking ID for invoice: ");
            int bookingId = scan.nextInt();
            scan.nextLine();

            double baseAmount = bookingDAO.getBookingAmount(bookingId);

            double serviceCharges = baseAmount * 0.05; // 5%
            double tax = baseAmount * 0.12; // 12%
            double total = baseAmount + serviceCharges + tax;

            invoiceDAO.createInvoice(
                    LocalDateTime.now(),
                    baseAmount,
                    serviceCharges,
                    tax,
                    total,
                    bookingId
            );

            System.out.println("\n--- INVOICE GENERATED ---");
            System.out.println("Room Charges: $" + baseAmount);
            System.out.println("Service Charges (5%): %" + serviceCharges);
            System.out.println("Tax (12%): $" + tax);
            System.out.println("TOTAL: $" + total);

        } catch (Exception e) {
            System.out.println("Invoice Error: " + e.getMessage());
        }
    }

    public static void viewInvoices(int userId) {
        try {
            InvoiceDAO dao = new InvoiceDAO();
            ResultSet rs = dao.getInvoicesByUser(userId);

            System.out.println("\n--- INVOICE HISTORY ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("invoice_id") + " | Booking: " +
                    rs.getInt("booking_id") + " | Total: $" +
                    rs.getDouble("total_amount") + " | " +
                    rs.getString("invoice_date")
                );
            }

            if (!found) System.out.println("No invoices found!");

        } catch (Exception e) {
            System.out.println("View Invoice Error: " + e.getMessage());
        }
    }
}
