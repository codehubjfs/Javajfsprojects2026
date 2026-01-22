package ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import dao.EmployerDAO;
import dao.UserDAO;
import services.EmployerServices;

public class EmployerUI {
	static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy");
	static DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("hh:mm");

	 
	public static void menu(String email,int user_id) {
		
		
		Scanner input = new Scanner(System.in);
		EmployerServices service = new EmployerServices();
		
		int choice=0;
		do {		
			System.out.println("""
					---- Employer----
					1. View profile
					2. Post Job
					3. Update the Job
					4. Delete the Job
					5. view Posted Job
					6. view Application
					7. Change the Application status
					8. Send Notification
					9. View Notification
					10. Request for verification
					11. Log out
					""");
			
			while(true) {
				try {
					System.out.print("Enter the Choice:");
					choice = input.nextInt();
					break;
				}
				catch(InputMismatchException e) {
					System.out.println("Choice should be numeric try again...");
					input.nextLine();
				}
			}
			
			switch (choice) {
			case 1:
			    service.displayProfile(email);
			    break;

			case 2:
			    if(!new EmployerDAO().validateEmployer(user_id)) {
			    	System.out.println("You are not verified by Admin...make a request for verification");
			    }
			    else {
			    	int company_id = new EmployerDAO()
				            .viewProfile(email)
				            .getCompany_id();

				    int payType;
				    while (true) {
				        System.out.println("""
				                Enter the Pay Type
				                1. Monthly
				                2. Yearly
				                """);

				        try {
				            payType = input.nextInt();
				            input.nextLine();

				            if (payType < 1 || payType > 2) {
				                System.out.println("Enter only 1 or 2");
				            } else {
				                break;
				            }
				        } catch (InputMismatchException e) {
				            System.out.println("Enter value correctly");
				            input.nextLine();
				        }
				    }

				    System.out.println("Enter the Job Title:");
				    String title = input.nextLine();

				    System.out.println("Enter the Job Description:");
				    String description = input.nextLine();

				    String location = new UserDAO()
				            .viewCompany(company_id)
				            .getLocation();

				    System.out.println("Enter the Experience Required:");
				    int experience = input.nextInt();
				    input.nextLine();

				    LocalDate deadline;
				    while (true) {
				        try {
				            System.out.println("Enter the Deadline date (dd-MM-yyyy)");
				            String stringDeadline = input.nextLine().trim();

				            deadline = LocalDate.parse(stringDeadline, formatter);

				            if (deadline.isBefore(LocalDate.now())) {
				                System.out.println("Deadline must be after today. Try again.");
				            } else {
				                break;
				            }
				        } catch (DateTimeParseException e) {
				            System.out.println("Invalid date format. Use dd-MM-yyyy");
				        }
				    }

				    service.postJob(
				            company_id,
				            title,
				            description,
				            location,
				            experience,
				            LocalDate.now(),
				            deadline,
				            payType
				    );

				    System.out.println("""
				            Job posted successfully....
				            ---------------------------------------------
				            """);
			    }
			    break; 
			case 5:
				
				service.viewPotedJob(new EmployerDAO()
			            .viewProfile(email)
			            .getCompany_id());
				break;
			case 9:
			    service.viewNotification(user_id);
			    break;
			case 10:
				service.requestForVerification(user_id);
				break;
			case 11:
			    try {
					System.out.println("Logging out.... \nLogout Time: "
					        + LocalTime.now().format(timeformatter) + "\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    break;

			default:
			    System.out.println("Currently we are in development");
			}

			
		
		}while(choice!=11);
	}
}
