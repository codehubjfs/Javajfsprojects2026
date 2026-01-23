package util;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import Exceptions.EmptyInputException;
import Exceptions.InputMismatchException;

public class InputValidate {
	//Method to validate the menu choices
	public static int ChoiceValidation(Scanner s,int min,int max) throws EmptyInputException, InputMismatchException {
		while(true) {
			String input = s.nextLine().trim();
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			//number check
			if(!input.matches("[0-9]+")) {
				throw new InputMismatchException("Input must be an Integer.");
			}
			
			int choice = Integer.parseInt(input);
			
			//range check
			if(choice < min || choice > max) {
				throw new InputMismatchException("Please enter a valid Option.");
			}
			
			return choice;
		}
	}
	
	//Method to validate Int inputs
	
	public static int IntValidation(Scanner s,String prompt) throws EmptyInputException, InputMismatchException {
		while(true) {
			System.out.println(prompt);
			String input = s.nextLine().trim();
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			//number check
			if(!input.matches("[0-9]+")) {
				throw new InputMismatchException("Input must be an Integer.");
			}
			
			int value = Integer.parseInt(input);
			
			if(value <= 0) {
				throw new InputMismatchException("Value should be greater than 0.Try Again.");
			}
			
			return value;
		}
	}
	
	
	//Method to validate String Input
	public static String StringValidation(Scanner s,String prompt) throws EmptyInputException, InputMismatchException {
		while(true) {
			System.out.println(prompt);
			String input = s.nextLine().trim();
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			//Invalid Char check
			if(!input.matches("[a-zA-Z -]+")) {
				throw new InputMismatchException("Invalid Characters Detected.Try Again.");
			}
			
			
			return input;
		}
	}
	
	
	//Method to validate Double Input
	public static double DoubleValidation(Scanner s,String prompt,double min,double max) throws EmptyInputException, InputMismatchException {
		while(true) {
			System.out.println(prompt);
			String input = s.nextLine().trim();
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			if(!input.matches("[0-9]+(\\.[0-9]+)?")) {
				throw new InputMismatchException("Enter a valid value.");
			}
			
			double value = Double.parseDouble(input);
			
			if(value < min || value > max) {
				throw new InputMismatchException("Value must be in range between "+min+" - "+max);
			}
			return value;
		}
		
	}
	
	
	//Method to validate date
	public static String DateValidation(Scanner s,String prompt) throws EmptyInputException, InputMismatchException {
		while(true) {
			System.out.println(prompt);
			String input = s.nextLine().trim();
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			try {
				LocalDate.parse(input);
			}catch(DateTimeParseException e) {
				throw new InputMismatchException("Invalid Date Formate.Use yyyy-MM-dd");
			}
			
			return input;
		}
	}
	
	
	//Method to validate for descriptions and urls
	public static String DescURLValidation(Scanner s,String prompt) throws EmptyInputException,InputMismatchException{
		while(true) {
			System.out.println(prompt);
			String input = s.nextLine().trim();
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			//Invalid Char check
			if(!input.matches("[a-zA-Z0-9 ._\\-/]+")) {
				throw new InputMismatchException("Invalid Characters Detected.Try Again.");
			}
			
			
			return input;
		}
	}
	
	
	//method to validate for email address
	public static String EmailValidation(Scanner s,String prompt) throws EmptyInputException,InputMismatchException {
		System.out.println(prompt);
		String input = s.nextLine().trim();
		
		//null check
		if(input.isEmpty()) {
			throw new EmptyInputException("Email cannot be empty");
		}
		
		if(!input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			throw new InputMismatchException("Invalid email format.");
		}
		
		return input;
	}
	
	
	//method to validate for password
	public static String PasswordValidation(Scanner s,String prompt) throws EmptyInputException{
		System.out.println(prompt);
		String input = s.nextLine().trim();
		
		if(input.isEmpty()) {
			throw new EmptyInputException("Password cannot be empty");
		}
		
		return input;
	}
}
