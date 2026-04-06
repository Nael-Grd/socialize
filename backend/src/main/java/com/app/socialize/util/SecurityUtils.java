package com.app.socialize.util;

import org.springframework.security.core.context.SecurityContextHolder;
import com.app.socialize.model.User;

public class SecurityUtils {

    public static String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
        
        if (principal instanceof User) {
            return ((User) principal).getEmail();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}