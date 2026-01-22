package com.util;

public final class ValidationUtil {

	private ValidationUtil() {}
	
	public static boolean isValidFirstName(String firstName) {
		return firstName.matches("[A-Za-z]+{2,30}"); 
	}
	
	public static boolean isValidLastName(String lastName) {
		return lastName.matches("[A-Za-z]{1,30}");
	}
	
	public static boolean isValidEmail(String email) {
		return email.matches("^[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]{1,64}@[a-zA-Z0-9.-]{2,253}\\.[a-zA-Z]{2,10}$");
	}
	
	public static boolean isValidPassword(String password) {
		return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,20}$");
	}
	
	public static boolean isValidPhone(String phone) {
		return phone.matches("\\d{10}");
	}
	
	public static boolean isValidName(String name) {
		return name.matches("[A-Za-z ]{2,}");
	}
	
	public static boolean isValidDescription(String description) {
		return description.matches("^(?=.*[A-Z])(?=.*[a-z])[A-Za-z0-9!@#$%^*&(){}\\[\\]\"'=+\\-]{5,}$");
	}
	
	public static boolean isNonNegative(double value) {
		return value > 0;
	}
	
}
