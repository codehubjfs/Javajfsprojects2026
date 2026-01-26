package UI;

import java.util.Scanner;

import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import Exceptions.InvalidCredentialsException;
import ServiceLayer.AuthService;
import util.InputValidate;

public class LoginUI {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int choice = -1;
		System.out.println("-------------------------Login-----------------------");
		
		do {
			try {
				System.out.println("1.Login as Customer");
				System.out.println("2.Login as Admin");
				System.out.println("3.Exit");
				System.out.print("Please enter your choice: ");
				
				choice = InputValidate.ChoiceValidation(s, 1, 3);
				System.out.println();
				
				switch(choice) {
				
				case 1:
					int attempts = 0;
					while(attempts < 3) {
						try {
							String email = InputValidate.EmailValidation(s, "Enter Customer email: ");
							String password = InputValidate.PasswordValidation(s, "Enter Customer password: ");
							Model.Customer customer = AuthService.customerLogin(email, password);
							System.out.println("\nLogin Successful.");
							Customer.customerMenu(customer);
							return;
					}catch(InvalidCredentialsException e) {
						attempts++;
						System.out.println(e.getMessage());
						System.out.println("Please try Again.");
						System.out.println();
					}catch(EmptyInputException | InputMismatchException e) {
						System.out.println(e.getMessage());
						System.out.println();
					}catch(DBAccessException e) {
						System.out.println("Error: "+e.getMessage());
						return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
						System.out.println();
					}
				}
				System.out.println("More than 3 failed attempts.Exiting");
				return;
				case 2:
					int adminAttempts = 0;
					while(adminAttempts < 3) {
						try {
							String email = InputValidate.EmailValidation(s, "Enter admin email: ");
							String password = InputValidate.PasswordValidation(s, "Enter admin password: ");
							Model.Admin admin = AuthService.adminLogin(email, password);
							System.out.println();
							System.out.println("Login Successful.Welcome "+admin.getName());
							System.out.println();
							
							Admin.menu();
							return;
						}catch(InvalidCredentialsException e) {
							adminAttempts++;
							System.out.println(e.getMessage());
							System.out.println("Please try Again.");
							System.out.println();
						}catch(EmptyInputException | InputMismatchException e) {
							System.out.println(e.getMessage());
							System.out.println();
						}catch(DBAccessException e) {
							System.out.println("Error: "+e.getMessage());
							return;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println(e.getMessage());
							System.out.println();
						}
					}
					System.out.println("More than 3 failed attempts.Exiting");
					return;
				case 3:
					System.out.println("Thank You!");
					break;
				}
				}catch(EmptyInputException | InputMismatchException e) {
					System.out.println(e.getMessage());
					System.out.println();
				}
			}while(choice != 3);
		s.close();
		}
}
