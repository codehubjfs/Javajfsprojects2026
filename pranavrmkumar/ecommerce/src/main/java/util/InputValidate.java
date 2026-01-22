package util;
import java.util.Scanner;

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
			if(!input.matches("\\d+")) {
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
			if(!input.matches("\\d+")) {
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
			
			if(!input.matches("\\d+(\\.\\d+)?")) {
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
			
			if(!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
				throw new InputMismatchException("Incorrect date format.Use format yyyy-mm-dd");
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
			if(!input.matches("[a-zA-Z0-9 ._\\-]+")) {
				throw new InputMismatchException("Invalid Characters Detected.Try Again.");
			}
			
			
			return input;
		}
	}
}
