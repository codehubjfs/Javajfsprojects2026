package service;

import java.util.Scanner;

public class UserService {
	public static void redirect() {
		Scanner scanner = new Scanner(System.in);
		int option = 0;
		
		do {
			System.out.println("1.Register");
			System.out.println("2.Login");
			System.out.println("3.Exit");
			System.out.println("\nEnter the options:");
			option = scanner.nextInt();
			switch(option) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			default:
				System.out.println("Enter valid option");
			
			}
		}while(option != 3);
		
		scanner.close();
		
	}
	
	public static void register() {
		Scanner scanner = new Scanner(System.in);
		String phoneNumber;
		System.out.println("Enter your phone number: ");
		phoneNumber = scanner.nextLine();
		
		
	}
}
