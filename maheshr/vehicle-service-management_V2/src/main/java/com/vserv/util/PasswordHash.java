package com.vserv.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password hashing utility using BCrypt
 * 
 * @author Mahesh R
 */
public class PasswordHash {
    
    /**
     * Hash password using BCrypt
     * 
     * @param plainPassword plain text password
     * @return bcrypt hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verify password against bcrypt hash
     * 
     * @param plainPassword plain text password to check
     * @param hashedPassword bcrypt hash from database
     * @return true if password matches
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
    
    private PasswordHash() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}