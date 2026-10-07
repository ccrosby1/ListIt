/**
 * AdminUserEntity.java
 * Database entity for the admin_user table
 */
package com.gcu.data.entity;

/**
 * This class represents a admin user entity in the database.
 * It contains fields for user information and provides getters and setters for each field.
 * The class is annotated with @Table to specify the database table name and @Column to specify the column names.
 */
public class AdminUserEntity {

	private int user_id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String createdDate;
	private String updatedDate;
	private String username;
	private String password;
	
	private int roles_id;
	
	/**
	 * Default constructor
	 */
	public AdminUserEntity() {
		this.user_id = 0;
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.phone = "";
		this.createdDate = "";
		this.updatedDate = "";
		this.username = "";
		this.password = "";
		this.roles_id = 2;
	}
	/**
	 * Constructor with parameters
	 * @param user_id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param createdDate
	 * @param updatedDate
	 * @param username
	 * @param password
	 */
	public AdminUserEntity(int user_id, String firstName, String lastName, String email, String phone,
			String createdDate, String updatedDate, String username, String password, int roles_id) {
		this.user_id = user_id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.username = username;
		this.password = password;
		this.roles_id = roles_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
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
	public int getRole() {
		return roles_id;
	}
	public void setRole(int role) {
		this.roles_id = role;
	}
}
