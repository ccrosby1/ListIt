/**
 * LoginFailureHandeler.java
 * 
 * Allows for logging of failed login events
 */
package com.gcu.utilities;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	/**
	 * Triggers logging for failed login events and redirects back to login page
	 * @param request the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @param authentication the Authentication object
	 * @throws IOException if an I/O error occurs
	 * @throws ServletException if a servlet error occurs
	 */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
                                        throws IOException, ServletException {

        String username = request.getParameter("username");

        CSVLogger.log(
                null,
                "AUTH",
                "LOGIN_FAILED",
                "Failed login attempt for '" + username + "'",
                exception.getMessage()
        );

        response.sendRedirect("/login/?error=true");
    }
}