package com.recharge.util;

import java.util.Scanner;

public class InputUtil {
	
	private static final Scanner sc = new Scanner(System.in);
	
	private InputUtil() {
		
	}
	
	// used to verify the input value strictly as integer only.
	public static int readInt(String message) {
		while(true) {
			try {
				System.out.println(message+ " ");
				return Integer.parseInt(sc.nextLine().trim());
			}
			catch(NumberFormatException e) {
				System.out.println("Invalid number. Please try again.");
			}
		}
	}
	
	// used to verify the range of input value that is within range.
	public static int readInt(String message, int min, int max) {
		while(true) {
			int value = readInt(message);
			if(value >= min && value <= max) {
				return value;
			}
			System.out.println("Please enter a value between "+min+ " and "+max);
			
		}
	}
	
	// used to verify the input string
	public static String readString(String message) {
		while(true) {
			System.out.println(message+" ");
			String input = sc.nextLine().trim();
			if(!input.isEmpty()) {
				return input;
			}
			System.out.println("Input cannot be empty. Please try again");
		}
	}

}
