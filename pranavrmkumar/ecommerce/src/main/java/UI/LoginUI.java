
package UI;

import java.util.Scanner;

import Exceptions.DBAccessException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;
import Exceptions.InvalidCredentialsException;
import ServiceLayer.AuthService;
import util.InputValidate;
 class Employee{
	 String name;
	 int age;
	 public static boolean hashcodeEquals(Object o1,Object o2) {
		 if(o1.hashCode() == (o2.hashCode())) {
			 return true;
		 }
		 else {
			 return false;
		 }
	 }
	public Employee(String name,int age) {
		this.name = name;
		this.age = age;
	}
}
public class LoginUI {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int choice = -1;
		System.out.println("---------------------------------------------------------------");
		System.out.println("-----------------------Ecommerce System------------------------");
		System.out.println("---------------------------------------------------------------");
		
		do {
			try {
				System.out.println("1.Login");
				System.out.println("2.Register as a new Customer");
				System.out.println("3.Exit");
				System.out.print("Please enter your choice: ");
				
				choice = InputValidate.ChoiceValidation(s, 1, 3);
				System.out.println();
				
				switch(choice) {
				
				case 1: // Login
				    int attempts = 0;
				    boolean loginSuccess = false;
				    while (attempts < 3) {
				        try {
				            String email = InputValidate.EmailValidation(s, "Enter email: ");
				            String password = InputValidate.PasswordValidation(s, "Enter password: ");
				            
				            Model.User user = AuthService.login(email, password); 
				            
				            System.out.println("\nLogin Successful.");
				            System.out.println("Welcome " + user.getName());
				            
				            if ("admin".equalsIgnoreCase(user.getRole())) {
				                AdminMenu.menu(s);
				            } else if ("customer".equalsIgnoreCase(user.getRole())) {
				                CustomerMenu.customerMenu(s, (Model.Customer) user);
				            }
				            
				            loginSuccess = true;
				            break;
				        } catch (InvalidCredentialsException e) {
				            attempts++;
				            System.out.println(e.getMessage());
				            System.out.println("Please try Again. (" + (3 - attempts) + " remaining)");
				            System.out.println();
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				            System.out.println();
				        } catch (DBAccessException e) {
				            System.out.println("Error: " + e.getMessage());
				            break;
				        } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				    if (attempts >= 3 && !loginSuccess) {
				        System.out.println("More than 3 attempts. Returning to Main Menu.");
				        System.out.println();
				    }
				    break;

					
				case 2:
				    System.out.println("------ Customer Registration ------");

				    String name = null;
				    while (true) {
				        try {
				            name = InputValidate.NameValidation(s, "Enter name: ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String email = null;
				    while (true) {
				        try {
				            email = InputValidate.EmailValidation(s, "Enter email: ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String password = null;
				    while (true) {
				        try {
				            password = InputValidate.PasswordValidation(s, "Enter password: ");
				            break;
				        } catch (EmptyInputException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String street = null;
				    while (true) {
				        try {
				            street = InputValidate.AddressValidation(s, "Enter street: ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String city = null;
				    while (true) {
				        try {
				            city = InputValidate.AddressValidation(s, "Enter city: ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String state = null;
				    while (true) {
				        try {
				            state = InputValidate.AddressValidation(s, "Enter state: ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String pincode = null;
				    while (true) {
				        try {
				            pincode = InputValidate.PincodeValidation(s, "Enter pincode: ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    String addressType = null;
				    while (true) {
				        try {
				            addressType = InputValidate.AddressTypeValidation(
				                    s, "Enter Address Type (home/office/other): ");
				            break;
				        } catch (EmptyInputException | InputMismatchException e) {
				            System.out.println(e.getMessage());
				        }
				    }

				    try {
				        boolean success = AuthService.registerCustomer(
				                name, email, password,
				                street, city, state, pincode, addressType
				        );

				        if (success) {
				            System.out.println("\nRegistration successful. Please login.");
				        }

				    } catch (DBAccessException e) {
				        System.out.println("Error: " + e.getMessage());
				    }
				    break;

					
				case 3:
					System.out.println("Thank You!");
					s.close();
					break;
				}
				}catch(EmptyInputException | InputMismatchException e) {
					System.out.println(e.getMessage());
					System.out.println();
				}
			}while(choice != 3);
		
		}
}
