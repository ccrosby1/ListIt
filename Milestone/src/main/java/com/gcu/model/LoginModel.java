/**
 * LoginModel
 * This class represents the login model for the application.
 * It contains the username and password fields, along with validation annotations.
 * It is used in the LoginController to bind the login form data.
 */
package com.gcu.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This class represents the login model for the application.
 * It contains the username and password fields, along with validation annotations.
 * It is used in the LoginController to bind the login form data.
 */
public class LoginModel {
	
	@NotNull(message = "Username is required")											
	@Size(min = 1, max=20, message = "User name must be between 1 and 20 characters")	
	private String username;
	
	@NotNull(message = "Password is required")											
	@Size(min = 1, max=20, message = "Password must be between 1 and 20 characters")	
	private String password;
	
	/**
	 * Default constructor for LoginModel
	 * Initializes the username and password fields to empty strings.
	 */
	public LoginModel() {				
		this.username = "";				
		this.password = "";				
	}
	/**
	 * Constructor for LoginModel with parameters
	 * @param username
	 * @param password
	 */
	public LoginModel(String username, String password) {	
		this.username = username;							
		this.password = password;							
	}
	/*************************************** Getters and Setters  *******************************************/
	public String getUsername() {
		return username;									
	}
	public void setUsername(String username) {
		this.username = username;							
	}
	public String getPassword() {
		return password;									
	}
	public void setPassword(String password) {
		this.password = password;							
	}

}
