package ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

import Exceptions.EmailFormateException;
import Exceptions.PasswordFormateException;
import Models.User;
import dao.UserDAO;
import services.UserServices;

public class LoginUI {

    public static void main(String[] args) {

    	DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("hh:mm");
        Scanner input = new Scanner(System.in);
        UserServices userService = new UserServices();
        UserDAO dao = new UserDAO();

        lable:
        while (true) {   

            int choice = 0;
            String role = "";

            
            while (true) {
            	System.out.println("""
            	        ---- LOGIN ----
            	        1. Admin
            	        2. Employer
            	        3. Job Seeker
            	        4. Exit
            	        """);
            	System.out.print("Select Role: ");

                if (input.hasNextInt()) {
                    choice = input.nextInt();
                    input.nextLine();

                    if (choice == 1) {
                        role = "ADMIN";
                        break;
                    } else if (choice == 2) {
                        role = "EMPLOYER";
                        break;
                    } else if (choice == 3) {
                        role = "JOB_SEEKER";
                        break;
                    }
                    else if(choice<=0 || choice >4) {
                    	// handling the edge cases
                    }
                    else {
                    	System.out.println("Thak you");
                    	break lable;
                    }
                } else {
                    input.nextLine();
                }

                System.out.println("Invalid role selection. Try again.\n");
            }

           
            String email;

            while (true) {
                System.out.print("Enter Email: ");
                email = input.nextLine();

                try {
                    userService.validateEmailFormate(email);

                    if (!dao.validUserRole(email, role)) {
                        System.out.println("Role does not match with email. Try again.\n");
                        continue;
                    }

                    break;

                } catch (EmailFormateException e) {
                    System.out.println(e.getMessage()); 
                    System.out.println("Try again...\n");
                }
            }

           
            while (true) {
                System.out.print("Enter Password: ");
                String password = input.nextLine();
                
                try {
					userService.validatePasswordFormate(password);
				} catch (PasswordFormateException e) {
					System.out.println(e.getMessage());
				}

                if (role.equals("ADMIN")) {
                    Optional<User> user = dao.loginEmployer(email, password);

                    if (user.isPresent()) {
                        System.out.println("\nWelcome  " + user.get().getName()+"\nLogin Time:"+LocalTime.now().format(formatter)+"\n");
                        AdminUI.menu(email,user.get().getId());   
                        break;           
                    } else {
                        System.out.println("Invalid password. Try again.\n");
                    }
                }
                
                else if(role.equals("EMPLOYER")) {
                	Optional<User> user = dao.loginEmployer(email, password);
                	
                	if(user.isPresent()) {
                		System.out.println("Welcom :"+user.get().getName()+"\nLogin Time: "+LocalTime.now().format(formatter)+"\n");
                		EmployerUI.menu(email,user.get().getId());
                		break;
                	}
                	else {
                		System.out.println("Invalid Password Try Again....\n");
                	}
                }
                else {
                    System.out.println("Login for this role is under development.\n");
                    break;
                }
            }
            
        }
    }
}