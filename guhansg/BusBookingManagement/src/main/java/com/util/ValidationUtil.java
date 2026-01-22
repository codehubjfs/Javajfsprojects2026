package com.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.exception.*;

public class ValidationUtil {
    public static void checkName(String name) throws InvalidNameException {
    	if(name == null) {
    		throw new InvalidNameException("Name cannot be null");
    	}
    	if(!name.matches("^[A-Za-z ]{1,20}$")) {
    		throw new InvalidNameException("Invalid Name,Alphabets only allowed");
    	}
    }
   
    public static void checkMobile(String mobile) throws InvalidMobileException {
        if (!mobile.matches("^[6-9][0-9]{9}$")) {
            throw new InvalidMobileException("Invalid mobile number");
        }
    }
    
    public static void checkEmail(String email) throws InvalidEmailException {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
    
    public static void checkPassword(String password) throws InvalidPasswordException {
        if (!password.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,}")) {
            throw new InvalidPasswordException(
                "Password must contain uppercase, lowercase & number (min 6 chars)"
            );
        }
    }
    
    public static LocalDate parseDate(String date) throws DateTimeParseException {
        return LocalDate.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalTime parseTime(String time) throws DateTimeParseException {
        return LocalTime.parse(time,
                DateTimeFormatter.ofPattern("HH-mm"));
    }
    
    public static void checkBusNo(String bus) throws InvalidBusNumberException {
    	if(!bus.matches("^[A-Z0-9]{10}")) {
    		throw new InvalidBusNumberException("Bus Number should be in given Standard Format (AA NN AA NNNN)");
    	}
    }
    
    public static void checkBusAvail(String busAvail) throws InvalidBusAvailException{
    	if(!((busAvail.equals("AVAILABLE")) || (busAvail.equals("ON TRIP")) || (busAvail.equals("NOT AVAILABLE")))){
    		throw new InvalidBusAvailException("Status not match to any given option");
    	}
    }
    
    public static int menuOptionCheck(String option) throws InvalidNumberFormat{
    	if(!option.matches("\\d+")) {
    		throw new InvalidNumberFormat("Option should be Integer");
    	}
    	return Integer.parseInt(option);
    }
}