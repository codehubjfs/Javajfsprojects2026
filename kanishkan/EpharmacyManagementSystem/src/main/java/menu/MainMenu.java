package menu;

import java.util.Scanner;
import authentication.*;


public class MainMenu {
	public static void mainMenu() throws Exception{
		System.out.println("*****************************************************************************");
		System.out.println("*                      E-PHARMACY MANAGEMENT SYSTEM                         *");
		System.out.println("*****************************************************************************");
		System.out.println("           Get your medicines fast with super fast delivery      ");
		
		int option = 0;
		Scanner scanner = new Scanner(System.in);
		
		do {
			String menu  = """
					1. Login
					2. Register
					3. Exit
					Enter your choice:
					""";

			System.out.print(menu);
			option = scanner.nextInt();
			switch(option) {
			case 1 -> Login.login();
			case 2 -> Register.register();
			case 3 -> System.exit(0);
			}
		}while(option != 3);
		
		scanner.close();
	}
}
