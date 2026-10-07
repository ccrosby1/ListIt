/**
 * UserModel
 * This class represents the user model for the application.
 * It contains fields for first name, last name, email, phone number, username, and password.
 * It also includes validation annotations to ensure that the fields are not null or blank.
 */
package com.gcu.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * This class represents the user model for the application.
 * It contains fields for first name, last name, email, phone number, username, and password.
 * It also includes validation annotations to ensure that the fields are not null or blank.
 */
public class UserModel {
	
	private String username;
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
	
	private Integer role;															
	private int userId;															
	private String createdDate;													
	private String updatedDate;													
	
	/*************************************** Constructors  *******************************************/
	/**
	 * Default Constructor for UserModel
	 * Initializes the firstName, lastName, email, phone, username and password fields to empty strings.
	 */
	public UserModel() {
		this.userId = 0;				
		this.firstName = "";	
		this.lastName = "";			
		this.email = "";			
		this.phone = "";			
		this.role = null;			
		this.username = "";
		
	}
	
	/**
	 * Constructor for UserModel with parameters
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param username
	 * @param password
	 */
	public UserModel(String firstName, String lastName, String email, 
			String phone, String username, String password) {
		this.userId = 0;				
		this.firstName = firstName;		
		this.lastName = lastName;			
		this.email = email;					
		this.phone = phone;				
		this.role = 0;					
		
	}	
	
	/*************************************** Getters and Setters  *******************************************/
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	public String getFirstName() { return firstName;} 							
	public void setFirstName(String firstName) { this.firstName = firstName;}	
	
	public String getLastName() { return lastName; }							
	public void setLastName(String lastName) { this.lastName = lastName; }		

	public String getEmail() { return email; }									
	public void setEmail(String email) { this.email = email; }			

	public String getPhone() { return phone; }								
	public void setPhone(String phone) { this.phone = phone; }				

	public Integer getRole() { return role; }										
	public void setRole(Integer role) { this.role = role; }						
	
	public int getUserId() { return userId; }									
	public void setUserId(int userId) { this.userId = userId; }				

	public String getCreatedDate() { return createdDate; }						
	public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }	
	public String getUpdatedDate() { return updatedDate; }					
	public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }
	
	
	
}
