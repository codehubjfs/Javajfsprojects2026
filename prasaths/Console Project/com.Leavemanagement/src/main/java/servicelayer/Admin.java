package servicelayer;
import java.util.*;
import DAO.AdminDao;
import Exceptions.*;

public class Admin {
	
	private static final Scanner sc = new Scanner(System.in);
	
	//---------------------------------main access control of the admin----------------------------------
	public static void accessControls() {
		
		boolean terminate = false;
		
		String text = "Enter 1 to Access employees\n"+
					  "Enter 2 to access Leavepolicy\n"+
					  "Enter 3 to access the leave balance\n"+
					  "Enter 4 to report generation\n"+
					  "Enter 5 to exit";
		
		do {
			try {
				System.out.println("===========================Admin access control=================================");
				System.out.println(text);
				
				System.out.println("Enter the choice:");
				String choice = sc.nextLine();
				if(!(choice.matches("^[0-9]$"))) {
					throw new InvalidInputException("The choice entry is wrong in the update leave policy");
				}
				
				switch(choice) {
					case "1":
						AccessEmployees.employeeAccess();
						break;
					case "2":
						AdminLeavePolicy.LeavePolicyAccess();;
						break;
					case "3":
						
						break;
					case "5":
						terminate = true;
						System.out.println("Return to the login page");
						break;
					default:
						System.out.println("Enter the correct choice");
				}
			}
			catch(InputMismatchException e) {
				System.out.println(e.toString());
			}
			catch(Exception e) {
				System.out.println(e.toString());
			}
			
		}
		while(!terminate);
		
	}
	
	/*public static void registerNewEmployee() {
		System.out.println("============================================================");
		System.out.println("Register the new employee");
		String sql = "INSERT INTO employee ("
				+ "  emp_id, first_name, last_name, date_of_birth, join_date,"
				+ "  employee_type, skill_set, street, city, state, pincode,"
				+ "  phone_number, email, gender, user_id, created_at"
				+ " ) VALUES"
				+ " (101, 'Admin', 'User', '1985-01-01', '2015-01-01',\r\n"
				+ " 'PERMANENT', 'React', 'Main Rd', 'Chennai', 'Tamil Nadu', '600001',\r\n"
				+ " '9000000001', 'admin@company.com', 'MALE', 1, NOW())"
		
	}*/
	

}
