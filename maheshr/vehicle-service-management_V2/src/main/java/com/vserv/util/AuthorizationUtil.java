package com.vserv.util;

import com.vserv.exception.AuthorizationException;
import com.vserv.model.User;

/**
 * Utility rbac
 * 
 * @author Mahesh R
 */
public class AuthorizationUtil {

    /**
     * Verify user has one of the allowed roles
     * 
     * @param user authenticated user
     * @param allowedRoles roles permitted for this operation
     * @throws AuthorizationException if user lacks required role
     */
    public static void requireRole(User user, String... allowedRoles) {
        if (user == null) {
            throw new AuthorizationException("User not authenticated");
        }

        for (String role : allowedRoles) {
            if (role.equals(user.getRoleName())) {
                return;
            }
        }

        throw new AuthorizationException(
            "Access denied. Required role: " + String.join(" or ", allowedRoles)
        );
    }
}
