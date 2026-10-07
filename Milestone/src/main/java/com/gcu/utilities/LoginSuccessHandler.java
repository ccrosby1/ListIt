/**
 * LoginSuccessHandler.java
 */
package com.gcu.utilities;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class handles successful authentication events.
 * It redirects users to different pages based on their roles.
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * This method is called when authentication is successful.
	 * @param request the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @param authentication the Authentication object
	 * @throws IOException if an I/O error occurs
	 * @throws ServletException if a servlet error occurs
	 */
    @Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
										HttpServletResponse response,
										Authentication authentication) 
										throws IOException, ServletException {
		
    	String username = authentication.getName();
    	
        CSVLogger.log(
                null,
                "AUTH",
                "LOGIN_SUCCESS",
                "User '" + username + "' authenticated successfully",
                ""
        );
        
		if(authentication
					.getAuthorities()
					.toString()
					.contains("ROLE_admin")) {		
			// redirect admin users to admin dashboard
			response.sendRedirect("/admin/dashboard/");				
		} else if(authentication
					.getAuthorities()
					.toString()
					.contains("ROLE_user")) {			
			// redirect regular users to user dashboard
			response.sendRedirect("/user/dashboard/");				
		}
	}
}