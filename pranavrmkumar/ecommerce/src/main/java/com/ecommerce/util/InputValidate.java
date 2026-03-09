package com.ecommerce.util;
import java.util.Scanner;

import com.ecommerce.exceptions.EmptyInputException;
import com.ecommerce.exceptions.InputMismatchException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputValidate {
	//Method to validate the menu choices
	public static int ChoiceValidation(Scanner s,int min,int max) throws EmptyInputException, InputMismatchException {
		while(true) {
			String input = s.nextLine().trim();
			if(input.length() > 10) {
				throw new InputMismatchException("Input is too large or invalid");
			}
			
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			//number check
			if(!input.matches("[0-9-]+")) {
				throw new InputMismatchException("Input must be an Integer.");
			}
			
			int choice = Integer.parseInt(input);
			
			//negative check
			if(choice < 0) {
				throw new InputMismatchException("Choice cannot be negative.");
			}
			
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
			
			if(input.length() > 10) {
				throw new InputMismatchException("Input is too large or invalid");
			}
			//null check
			if(input.isEmpty()) {
				throw new EmptyInputException("Input is empty.");
			}
			
			//number check
			if(!input.matches("[0-9-]+")) {
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
		if(max <= min) {
			throw new InputMismatchException("Max value cannot be lesser than min value.");
		}
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
			
			//negative check
			if(value < 0) {
				throw new InputMismatchException("Value cannot be negative.");
			}
			
			
			
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
			
			//not only numbers
			if (input.matches("[0-9]+")) {
	            throw new InputMismatchException("Input cannot contain only numbers.");
	        }
			return input;
		}
	}
	
	
	//method to validate for email address
	public static String EmailValidation(Scanner s,String prompt) throws EmptyInputException,InputMismatchException {
		while(true) {
		System.out.println(prompt);
		String input = s.nextLine().trim();
		
		//null check
		if(input.isEmpty()) {
			throw new EmptyInputException("Email cannot be empty");
		}
		
		if(!input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			throw new InputMismatchException("Invalid email format (Email format: abc@example.com).");
		}
		
		return input;
		}
	}
	
	
	//method to validate for password
	public static String PasswordValidation(Scanner s,String prompt) throws EmptyInputException, InputMismatchException{
		while(true) {
		System.out.println(prompt);
		String input = s.nextLine().trim();
		
		if(input.isEmpty()) {
			throw new EmptyInputException("Password cannot be empty");
		}
		if(!input.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
			throw new InputMismatchException("Invalid Password Format(Should contain atleast 1 uppercase,1 lowercase,1 digit,1 special character and must be atleast 6 characters long");
		}
		
		return input;
		}
	}
	
	
	
	
	public static String NameValidation(Scanner s, String prompt)
	        throws EmptyInputException, InputMismatchException {

	    while (true) {
	        System.out.println(prompt);
	        String input = s.nextLine().trim();

	        if (input.isEmpty()) {
	            throw new EmptyInputException("Name cannot be empty.");
	        }

	        // Only letters and spaces
	        if (!input.matches("[A-Za-z ']+")) {
	            throw new InputMismatchException("Name can contain only letters and spaces.");
	        }

	        return input;
	    }
	}

	
	
	public static String AddressValidation(Scanner s, String prompt)
	        throws EmptyInputException, InputMismatchException {

	    while (true) {
	        System.out.println(prompt);
	        String input = s.nextLine().trim();

	        if (input.isEmpty()) {
	            throw new EmptyInputException("Address field cannot be empty.");
	        }

	        // Valid address characters
	        if (!input.matches("[A-Za-z0-9 ,./-]+")) {
	            throw new InputMismatchException("Invalid characters in address.");
	        }

	        // Prevent only numbers
	        if (input.matches("\\d+")) {
	            throw new InputMismatchException("Address cannot contain only numbers.");
	        }

	        return input;
	    }
	}

	
	
	public static String PincodeValidation(Scanner s, String prompt)
	        throws EmptyInputException, InputMismatchException {

	    while (true) {
	        System.out.println(prompt);
	        String input = s.nextLine().trim();

	        if (input.isEmpty()) {
	            throw new EmptyInputException("Pincode cannot be empty.");
	        }

	        // Exactly 6 digits
	        if (!input.matches("\\d{6}")) {
	            throw new InputMismatchException("Pincode must be exactly 6 digits.");
	        }

	        return input;
	    }
	}
	
	public static String AddressTypeValidation(Scanner s,String prompt) throws EmptyInputException,InputMismatchException{
		while(true) {
			System.out.println(prompt);
			String input = s.nextLine().trim().toLowerCase();
			if (input.isEmpty()) {
	            throw new EmptyInputException("Pincode cannot be empty.");
	        }
			if(!input.equals("home") && !input.equals("office") && !input.equals("other")) {
				throw new InputMismatchException("Please select a valid option: ");
			}
			
			if(!input.matches("[A-Za-z ]+")) {
				throw new InputMismatchException("Address Type cannot be a numeric value.");

			}
			
			return input;
		}
	}

}
