package authentication;

import java.util.Scanner;
import dao.LoginCredentialValidationDao;
import model.User;
import menu.*;

public class Login {
	
	public static void login() throws Exception{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter phoneNumber:");
		String phone = scanner.nextLine();
		System.out.println("Enter password;");
		String password = scanner.nextLine();
		User user = LoginCredentialValidationDao.validateLogin(phone, password);
		
		if(user != null) {
			System.out.println("Login Successfull!...");
			
			if(user.getRoleId() == 1) {
				AdminMenu admin = new AdminMenu();
				admin.showAdminMenu();
			}
			else if(user.getRoleId() == 2) {
				ManagerMenu manager = new ManagerMenu();
				
			}
		}
		else {
			System.out.println("Login Failed. Please enter valid credentials");
		}
		
	}

}
