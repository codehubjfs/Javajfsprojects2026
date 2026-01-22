import java.time.*;
import java.util.*;
import com.services.AdminService;
import com.services.OperatorService;
import com.util.ValidationUtil;
import com.dao.*;
import com.exception.*;

public class Main {

	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.println("=============== BUS TICKET BOOKING SYSTEM ================");
		boolean loop = true;
		
		while(loop) {
			System.out.println("1.Login\n2.Registration\n3.Exit\n");
			int option;
			while(true) {
				try {
					System.out.print("Enter Your Option : ");
					option = ValidationUtil.menuOptionCheck(sc.nextLine());
					break;
				}catch(InvalidNumberFormat e) {
					System.out.println(e.getMessage());
				}
			}
			System.out.println("------------------------------------------------------------");
			
			switch(option) {
			case 1: 
				boolean roleLoop = true;
				while(roleLoop) {
					System.out.println("Login as");
					System.out.println("1.Admin\n2.Operator\n3.Customer\n4.Exit\n");
					int roleOption;
					while(true) {
						try {
							System.out.print("Enter Your Option : ");
							roleOption = ValidationUtil.menuOptionCheck(sc.nextLine());
							break;
						}catch(InvalidNumberFormat e) {
							System.out.println(e.getMessage());
						}
					}
					switch(roleOption) {
					case 1:
						String adminEmail;String adminPassword;
						while(true) {
							try {
								System.out.print("Enter Your Email : ");
								adminEmail = sc.nextLine();
								ValidationUtil.checkEmail(adminEmail);
								break;
							}catch(InvalidEmailException e) {
								System.out.println(e.getMessage());
							}
						}
						while(true) {
							try {
								System.out.print("Enter the password : ");
								adminPassword = sc.nextLine();
								ValidationUtil.checkPassword(adminPassword);
								break;
							}catch(InvalidPasswordException e) {
								System.out.println(e.getMessage());
							}
							
						}
						
						String checkAdmin = UserDAO.checkingAdminLogin(adminEmail,adminPassword);
						if(checkAdmin != null) {
							 System.out.println("==================================================================");
							 System.out.println("Login Successful...!");
						     System.out.println("Welcome Admin, "+checkAdmin);
						     System.out.println("Your Login Time : "+LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
						     System.out.println("==================================================================");
						     boolean funcLoop = true;
						     while(funcLoop) {
						    	 System.out.println(
							                "1.Manage Route\n" +
							                "2.Manage Operator\n"+
							                "3.Generate Report\n" +
							                "4.Customer Feedback\n" +
							                "5.Exit\n"
							      );
						    	  int funcOption;
									while(true) {
										try {
											System.out.print("Enter Your Option : ");
											funcOption = ValidationUtil.menuOptionCheck(sc.nextLine());
											break;
										}catch(InvalidNumberFormat e) {
											System.out.println(e.getMessage());
										}
									}
						   
						    	 switch(funcOption) {
						    	 
						    	 case 1:
						    		 AdminService.manageRoute();
						    		 break;
						    		 
						    	 case 2:
						    		 AdminService.manageOperator();
						    		 break;
						    	 case 3:
						    		 System.out.println("Generate Report,under development");
						    		 break;
						    		 
						    	 case 4:
						    		 AdminService.viewFeedback();
						    		 break;
						    		 
						    	 case 5:
						    		 funcLoop = false;
						    		 break;
						    		 
						    	default:
						    		System.out.println("Invalid Option");
						    	 }
						    	 		 	 
						     }

						}
						else {
							System.out.println("Login Failed, Retry");
							System.out.println("-----------------------------------------------------");
						}
						break;
					case 2:
						String opEmail;String opPassword;
						while(true) {
							try {
								System.out.print("Enter Your Email : ");
								opEmail = sc.nextLine();
								ValidationUtil.checkEmail(opEmail);
								break;
							}catch(InvalidEmailException e) {
								System.out.println(e.getMessage());
							}
						}
						while(true) {
							try {
								System.out.print("Enter Your Password : ");
								opPassword = sc.nextLine();
								ValidationUtil.checkPassword(opPassword);
								break;
							}catch(InvalidPasswordException e) {
								System.out.println(e.getMessage());
							}
							
						}
						
						int checkOperator = UserDAO.checkingOperatorLogin(opEmail, opPassword);
						
						if(checkOperator != -1) {
							 System.out.println("==================================================================");
							 System.out.println("Login Successful...!");
						     System.out.println("Welcome Operator, "+checkOperator);
						     System.out.println("Your Login Time : "+LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
						     System.out.println("==================================================================");
						    boolean opFuncLoop = true;
						    while(opFuncLoop) {
						    	System.out.println("1.Manage Bus\n"
										+ "2.Assign Routes to Bus\n"
										+ "3.View Assigned Route\n"
										+ "4.View Booking\n"
										+ "5.Update Bus Availability\n"
										+ "6.Exit\n");
						    	int opFuncOption;
								while(true) {
									try {
										System.out.print("Enter Your Option : ");
										opFuncOption = ValidationUtil.menuOptionCheck(sc.nextLine());
										break;
									}catch(InvalidNumberFormat e) {
										System.out.println(e.getMessage());
									}
								}
								switch(opFuncOption) {
								case 1:
									OperatorService.manageBus(checkOperator);
									break;
								
								case 2:
									System.out.println("Assign route");
									break;
									
								case 3:
									System.out.println("view Assign route");
									break;
									
								case 4:
									System.out.println("View Booking");
									break;
									
								case 5:
									System.out.println("update bus avail");
									break;
									
								case 6:
									opFuncLoop = false;
									break;
									
								default :
									System.out.println("Invalid Option, Try again");

								}		
							}
					   }
					   else {
						    System.out.println("Login Failed, Retry");
						    System.out.println("---------------------------");
						}		
						break;
					case 3:
						System.out.println("Customer");
						break;
						
					case 4:
						roleLoop = false;
						break;
					default:
						System.out.println("Invalid Option, try again");
						
					}
				}
				break;
			case 2:
				System.out.println("Register as");
				System.out.println("1.Customer\n2.Operator\n");
				System.out.print("Enter Your Option : ");
				int registerAs;
				while(true) {
					try {
						System.out.print("Enter Your Option : ");
						registerAs = ValidationUtil.menuOptionCheck(sc.nextLine());
						break;
					}catch(InvalidNumberFormat e) {
						System.out.println(e.getMessage());
					}
				}
				switch(registerAs) {
				case 1:
					sc.nextLine();
					String custName;String custMobile;String custEmail;
	    			String custPassword;
	    			while(true) {
						try {
							System.out.print("Enter Your Name : ");
							custName = sc.nextLine();
							ValidationUtil.checkName(custName);
							break;
						}catch(InvalidNameException e) {
							System.out.println(e.getMessage());
						}
				  }
    			  while(true) {
						try {
							System.out.print("Enter Your Email : ");
							custEmail = sc.nextLine();
							ValidationUtil.checkEmail(custEmail);
							break;
						}catch(InvalidEmailException e) {
							System.out.println(e.getMessage());
						}
			     }
				 while(true) {
					   try {
							System.out.print("Enter Your password : ");
							custPassword = sc.nextLine();
							ValidationUtil.checkPassword(custPassword);
							break;
						}catch(InvalidPasswordException e) {
							System.out.println(e.getMessage());
						}
						
				  }
				  while(true) {
						try {
							System.out.print("Enter Your mobile : ");
							custMobile = sc.nextLine();
							ValidationUtil.checkMobile(custMobile);
							break;
						}catch(InvalidMobileException e) {
							System.out.println(e.getMessage());
						}
				  }
					UserDAO.customerRegistration(custName, custMobile, custEmail, custPassword, LocalDate.now());
					System.out.println("Your Details Registered Successfully...!");
					System.out.println("=========================================");
					break;
				
				case 2:
					sc.nextLine();
					String opName;String opMobile;String opEmail;
	    			String opPassword;
					while(true) {
						try {
							System.out.print("Enter Your Name : ");
							opName = sc.nextLine();
							ValidationUtil.checkName(opName);
							break;
						}catch(InvalidNameException e) {
							System.out.println(e.getMessage());
						}
				  }
    			  while(true) {
						try {
							System.out.print("Enter Your Email : ");
							opEmail = sc.nextLine();
							ValidationUtil.checkEmail(opEmail);
							break;
						}catch(InvalidEmailException e) {
							System.out.println(e.getMessage());
						}
			     }
				 while(true) {
					   try {
							System.out.print("Enter Your password : ");
							opPassword = sc.nextLine();
							ValidationUtil.checkPassword(opPassword);
							break;
						}catch(InvalidPasswordException e) {
							System.out.println(e.getMessage());
						}
						
				  }
				  while(true) {
						try {
							System.out.print("Enter Your mobile : ");
							opMobile = sc.nextLine();
							ValidationUtil.checkMobile(opMobile);
							break;
						}catch(InvalidMobileException e) {
							System.out.println(e.getMessage());
						}
				  }
					UserDAO.addNewOperator(opName, opMobile, opEmail, opPassword, LocalDate.now());
					System.out.println("Your Details Registered Successfully...!");
					System.out.println("=========================================");
					break;
				
				default:
					System.out.println("Invalid Option, try again");
				}	
				break;
			case 3:
				loop = false;
				System.out.println("Thank you for using Bus Ticket Booking...!");
				break;
			default:
				System.out.println("Invalid Option, try again");
			}	
			
		}
		sc.close();
	}

}
