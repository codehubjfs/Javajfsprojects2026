package UI;

import java.util.Scanner;

import Exceptions.DataAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import Exceptions.InvalidCredentialsException;
import Model.User;
import ServiceLayer.AuthService;
import util.InputValidate;

public class LoginUI {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int choice = -1;
		System.out.println("-------------------------Login-----------------------");
		
		do {
			try {
				System.out.println("1.Login as Admin");
				System.out.println("2.Exit");
				System.out.print("Please enter your choice: ");
				
				choice = InputValidate.ChoiceValidation(s, 1, 2);
				System.out.println();
				
				switch(choice) {
				case 1:
					int attempts = 0;
					while(attempts < 3) {
						try {
							String email = InputValidate.EmailValidation(s, "Enter admin email: ");
							String password = InputValidate.PasswordValidation(s, "Enter admin password: ");
							User admin = AuthService.adminLogin(email, password);
							System.out.println();
							System.out.println("Login Successful.Welcome "+admin.getName());
							System.out.println();
							
							Admin.menu();
							return;
						}catch(InvalidCredentialsException e) {
							attempts++;
							System.out.println(e.getMessage());
							System.out.println("Please try Again.");
							System.out.println();
						}catch(EmptyInputException | InputMismatchException e) {
							System.out.println(e.getMessage());
							System.out.println();
						}catch(DataAccessException e) {
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
					System.out.println("Thank You!");
					break;
				}
				}catch(EmptyInputException | InputMismatchException e) {
					System.out.println(e.getMessage());
					System.out.println();
				}
			}while(choice != 2);
		s.close();
		}
}
