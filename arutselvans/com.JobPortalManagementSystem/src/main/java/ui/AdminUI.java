package ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import services.AdminServices;

public class AdminUI {

    private static final Scanner sc = new Scanner(System.in);

    /* ================= FILTER MENU ================= */

    public static void filterMenu() {

        AdminServices admin = new AdminServices();
        int choice;

        do {
            System.out.println("""
                -------- FILTER MENU --------
                1. Display users based on Date of Birth
                2. Display users based on Gender
                3. Display users based on Role
                4. Display users based on Location
                5. Go back to Menu
                Enter Choice:
                """);

            while (!sc.hasNextInt()) {
                System.out.println("Invalid choice. Enter a number.");
                sc.nextLine();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> admin.sortBasedOnDateOfBirth();
                case 2 -> admin.displayuserBasedOnGender();
                case 3 -> admin.displayBasedOnUserRole();
//                case 4 -> admin.displayUserBasedOnLocation();
                case 5 -> System.out.println("Returning to Admin Menu...");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 5);
    }

    /* ================= ADMIN MENU ================= */

    public static void menu(String email, int user_Id) {

        AdminServices admin = new AdminServices();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        int ch;

        do {
            System.out.println("""
                -------- ADMIN DASHBOARD --------
                1. View All Users
                2. View All Companies
                3. View All Jobs
                4. Block User
                5. View Reports
                6. Verify Employer
                7. Send Announcement
                8. View Notifications
                9. Logout
                Enter choice:
                """);

            while (!sc.hasNextInt()) {
                System.out.println("Invalid choice. Enter a number between 1 and 9.");
                sc.nextLine();
            }

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    admin.viewAllUsers();
                    int filterChoice;

                    do {
                        System.out.println("""
                            1. Apply Filter
                            2. Go back
                            Enter your choice:
                            """);

                        while (!sc.hasNextInt()) {
                            System.out.println("Invalid input.");
                            sc.nextLine();
                        }

                        filterChoice = sc.nextInt();
                        sc.nextLine();

                        if (filterChoice == 1) {
                            filterMenu();
                        } else if (filterChoice != 2) {
                            System.out.println("Invalid choice.");
                        }

                    } while (filterChoice != 2);
                }

                case 2 -> admin.viewAllCompanies();

                case 3 -> admin.viewAllJobs();

                case 4 -> {
                    int userId;
                    System.out.print("Enter User ID to block: ");

                    while (!sc.hasNextInt()) {
                        System.out.println("Invalid User ID.");
                        sc.nextLine();
                    }

                    userId = sc.nextInt();
                    sc.nextLine();
                    admin.blockuser(userId);
                }

                case 5 -> System.out.println("Reports feature coming soon...");

                case 6 -> {
                    System.out.print("Enter Employer ID to verify: ");

                    while (!sc.hasNextInt()) {
                        System.out.println("Invalid Employer ID.");
                        sc.nextLine();
                    }

                    int id = sc.nextInt();
                    sc.nextLine();
                    admin.verifyEMployer(id);
                    System.out.println("Employer verified successfully.\n");
                }

                case 7 -> {
                    System.out.print("Enter Announcement Message: ");
                    String message = sc.nextLine();
                    admin.sendAnnounceMent(message);
                    System.out.println("Announcement sent successfully.\n");
                }

                case 8 -> admin.displayNotification(user_Id);

                case 9 -> System.out.println(
                        "Logging out...\nLogout Time: " +
                        LocalTime.now().format(formatter)
                );

                default -> System.out.println("Invalid choice.");
            }

        } while (ch != 9);
    }
}
