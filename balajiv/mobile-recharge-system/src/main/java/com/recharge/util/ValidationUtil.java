package com.recharge.util;

import java.util.regex.Pattern;

import com.recharge.exception.EmailFormatException;
import com.recharge.exception.MobileNumberFormatException;
import com.recharge.exception.PasswordFormatException;
import com.recharge.exception.UserNameFormatException;

public final class ValidationUtil {
	
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	
	private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");

	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9@#$%^&+=]{6,}$");
	
	private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z][A-za-z0-9_]{4,19}$");
	
	private ValidationUtil() {
		// prevent instantiation
	}
	
	/*
	 * used to check the email format
	 */
	public static boolean isValidEmail(String email) throws EmailFormatException {
		if (email == null || !EMAIL_PATTERN.matcher(email).matches()) { 
			throw new EmailFormatException("Invalid email format. Please enter a valid email like user@example.com.");
		} 
		return true;
	}
	
	/*
	 * used to check the mobile format
	 */
	public static boolean isValidMobile(String mobile) throws MobileNumberFormatException{
		if(mobile == null || !MOBILE_PATTERN.matcher(mobile).matches()) {
			throw new MobileNumberFormatException("Invalid mobile format. Please enter a valid mobile number like 9876543210");
		}
		return true;
	}
	
	/*
	 * used to check the password format
	 */
	public static boolean isValidPassword(String password) throws PasswordFormatException {
		if(password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
			throw new PasswordFormatException("Invalid Password Format. Password must be atleast length of 6");
		}
		return true;
	}
	
	/*
	 * used to check the not blank
	 */
	public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
	
	/*
	 * used to check username format
	 */
	public static boolean isValidUserName(String name) throws UserNameFormatException {
		if(name == null || !USERNAME_PATTERN.matcher(name).matches()) {
			throw new UserNameFormatException("Invalid Username Format");
		}
		return true;
	}

	/*
	 * used to check the gender 
	 */
	public static boolean isValidGender(String gender) {
		if(gender.equals("MALE") || gender.equals("FEMALE") || gender.equals("OTHER")) {
			return true;
		}
		return false;
	}
}
