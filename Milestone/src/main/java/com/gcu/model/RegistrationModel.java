/**
 * RegistrationModel
 * This class will handle the registration model for the application.
 * It contains fields for first name, last name, email, phone number, username, and password.
 * It also includes validation annotations to ensure that the fields are not null or blank.
 */
package com.gcu.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * This class represents the registration model for the application.
 * It contains fields for first name, last name, email, phone number, username, and password.
 * It also includes validation annotations to ensure that the fields are not null or blank.
 */
public class RegistrationModel {

	@NotBlank(message="First name is required")										
	private String firstName;	
	
	@NotBlank(message="Last name is required")										
	private String lastName;
	
	@NotBlank(message="Email is required")											
	@Email(message="Email is not valid")										
	private String email;
	
	@NotBlank(message="Phone number is required")								
	@Pattern(regexp = "^[0-9]{10}$",message = "Phone number must be exactly 10 digits")
	private String phone;
	
	@NotNull(message="Username is required")										
	@Size(min=1, max=32, message="User name must be between 1 and 32 characters")	
	private String username;
	
	@NotNull(message="Password is required")										
	@Size(min=1, max=32, message="Password must be between 1 and 32 characters")	
	private String password;
	
	@NotBlank(message="Confirm password is required")								
    private String confirmPassword;
	private String createdDate;
	private String updatedDate;
	private int userId;																
	private int role;															
	/*************************************** Constructors  *******************************************/
	/**
	 * Default Constructor for RegistrationModel
	 * Initializes the firstName, lastName, email, phone, username and password fields to empty strings.
	 */
	public RegistrationModel() {
		this.userId = 0;			
		this.firstName = "";	
		this.lastName = "";		
		this.email = "";			
		this.phone = "";		
		this.username = "";		
		this.password = "";			
		this.confirmPassword = "";	
		this.createdDate = "";		
		this.updatedDate = "";	
		this.role = 2;			
	}
	
	/**
	 * Constructor for RegistrationModel with parameters
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param username
	 * @param password
	 */
	public RegistrationModel(String firstName, String lastName, String email, 
			String phone, String username, String password, String confirmPassword) {
		this.userId = 0;				
		this.firstName = firstName;		
		this.lastName = lastName;		
		this.email = email;					
		this.phone = phone;					
		this.username = username;			
		this.password = password;			
		this.confirmPassword = "";			
		this.createdDate = "";				
		this.updatedDate = "";				
		this.role = 2;						
	}	
	
	/*************************************** Getters and Setters  *******************************************/
	public String getFirstName() {
		return firstName;					
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;			
	}
	
	public String getLastName() {
		return lastName;					
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;			
	}

	public String getEmail() {
		return email;						
	}

	public void setEmail(String email) {
		this.email = email;					
	}

	public String getPhone() {
		return phone;						
	}

	public void setPhone(String phone) {
		this.phone = phone;					
	}

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
	
	public String getConfirmPassword() {
		return confirmPassword;					
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;			
	}
	public int getUserId() {
		return userId;						
	}
	public void setUserId(int userId) {
		this.userId = userId;					
	}
	public String getCreatedDate() {
		return createdDate;					
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;			
	}
	public String getUpdatedDate() {
		return updatedDate;					
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;			
	}
	public int getRole() {
		return role;						
	}
	public void setRole(int role) {
		this.role = role;						
	}
	
}
