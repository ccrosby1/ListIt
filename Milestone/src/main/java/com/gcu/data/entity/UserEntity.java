/**
 * UserRepository
 */
package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * This class represents a user entity in the database.
 * It contains fields for user information and provides getters and setters for each field.
 * The class is annotated with @Table to specify the database table name and @Column to specify the column names.
 */
@Table("USER")
public class UserEntity {
	
	@Id
	@Column("user_id")
	private int user_id;	
	
	@Column("first_name")
	private String firstName;
	
	@Column("last_name")
	private String lastName;
	
	@Column("email")
	private String email;
	
	@Column("phone")
	private String phone;
	
	@Column("created_date")
	private String createdDate;
	
	@Column("updated_date")
	private String updatedDate;

	/*
	 * Default constructor for the UserEntity class
	 */
	public UserEntity() {
		this.user_id = 0;
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.phone = "";
		this.createdDate = "";
		this.updatedDate = "";
	}
	/**
	 * Constructor for the UserEntity class
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param createdDate
	 * @param updatedDate
	 */
	public UserEntity(int id,  String firstName,  String lastName,  String email,
					  String phone,  String createdDate, String updatedDate) {
		this.user_id= id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		
	}
	/**************************************** GETTERS AND SETTERS ****************************************/
	/* 
	 * Getter for the User ID
	 * This should never be changed after creation
	 * 
	 * @return user ID
	 */
	public int getId() { return this.user_id; }										
	public void setId(int id) { this.user_id = id; }								
	/**
	 * Setter for the User First Name
	 * This should never be changed after creation
	 * 
	 * @param firstName 
	 */
	public String getFirstName() { return firstName; }							
	/**
	 * Setter for the User First Name
	 * This should never be changed after creation
	 * 
	 * @param firstName 
	 */
	public void setFirstName(String firstName) { this.firstName = firstName; }	
	
	/**
	 * Setter for the User Last Name
	 * This should never be changed after creation
	 * 
	 * @param lastName 
	 */
	public String getLastName() { return lastName; }							
	/**
	 * Setter for the User Last Name
	 * This should never be changed after creation
	 * 
	 * @param lastName 
	 */
	public void setLastName(String lastName) { this.lastName = lastName; }		
	
	/**
	 * Setter for the User Email
	 * This should never be changed after creation
	 * 
	 * @param email 
	 */
	public String getEmail() { return email; }									
	/**
	 * Setter for the User Email
	 * This should never be changed after creation
	 * 
	 * @param email 
	 */
	public void setEmail(String email) { this.email = email; }					
	
	/**
	 * Setter for the User Phone
	 * This should never be changed after creation
	 * 
	 * @param phone 
	 */
	public String getPhone() { return phone; }									
	/**
	 * Setter for the User Phone
	 * This should never be changed after creation
	 * 
	 * @param phone 
	 */
	public void setPhone(String phone) { this.phone = phone; }					
	
	/**
	 * Setter for the User Created Date
	 * This should never be changed after creation
	 * 
	 * @param createdDate 
	 */
	public String getCreatedDate() { return createdDate; }						
	/**
	 * Setter for the User Created Date
	 * This should never be changed after creation
	 * 
	 * @param createdDate 
	 */
	public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }	
	
	/**
	 * Setter for the User Updated Date
	 * This should never be changed after creation
	 * 
	 * @param updatedDate 
	 */
	public String getUpdatedDate() { return updatedDate; }								
	/**
	 * Setter for the User Updated Date
	 * This should never be changed after creation
	 * 
	 * @param updatedDate 
	 */
	public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }	
}
