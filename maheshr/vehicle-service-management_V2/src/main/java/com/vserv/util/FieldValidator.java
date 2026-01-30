package com.vserv.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class FieldValidator {

	private static final Pattern FULL_NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s'-\\.]+$");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9+_.-]+@(.+)$");
	private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
	private static final Pattern REG_NUMBER_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$");

	public static boolean isValidFullName(String fullName) {
		if (fullName == null || fullName.trim().isEmpty()) {
			return false;
		}

		String trimmed = fullName.trim();

		if (trimmed.length() < 2 || trimmed.length() > 30) {
			return false;
		}

		return FULL_NAME_PATTERN.matcher(trimmed).matches();
	}

	public static boolean isValidEmail(String email) {
		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}

	public static boolean isValidPhone(String phone) {
		return phone != null && PHONE_PATTERN.matcher(phone).matches();
	}

	public static boolean isValidRegistrationNumber(String regNumber) {
		return regNumber != null && REG_NUMBER_PATTERN.matcher(regNumber).matches();
	}

	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*\\d)"
			+ "(?=.*[@$!%*?&#^()_+=\\-])" + "[A-Za-z\\d@$!%*?&#^()_+=\\-]{8,}$");

	public static boolean isFutureDate(LocalDate date) {
		return date != null && date.isAfter(LocalDate.now());
	}

	public static boolean isValidYear(int year) {
		int currentYear = LocalDate.now().getYear();
		return year >= 1900 && year <= currentYear;
	}

	public static boolean isValidPassword(String password) {
		return password != null && PASSWORD_PATTERN.matcher(password).matches();
	}
}
