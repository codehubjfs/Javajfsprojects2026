package com.vserv.exception;

/**
 * Exception thrown when user lacks required role permissions
 * 
 * @author Mahesh R
 */
public class AuthorizationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public AuthorizationException(String message) {
        super(message);
    }
}