package menu;

import java.util.Scanner;

public class MainMenu {

    private static final Scanner sc = new Scanner(System.in);

    public static void show() {
        while (true) {
            System.out.println("\n====== ONLINE MOVIE TICKET BOOKING ======");
            System.out.println("1. Guest");
            System.out.println("2. Customer");
            System.out.println("3. Front Officer");
            System.out.println("4. Admin");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> GuestMenu.show();
                case 2 -> CustomerMenu.show();
                case 3 -> FrontOfficerMenu.show();
                case 4 -> AdminMenu.show();
                case 0 -> {
                    System.out.println("Thank you. Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
