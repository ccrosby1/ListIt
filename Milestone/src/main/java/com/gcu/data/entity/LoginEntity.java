/**
 * UserRepository
 */
package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * This class represents a login entity in the database.
 * It contains fields for user information and provides getters and setters for each field.
 * The class is annotated with @Table to specify the database table name and @Column to specify the column names.
 */
@Table("user_credentials")
public class LoginEntity {
	
	@Id
	@Column("user_id")
	private int userId;
	
	@Column("username")
	private String username;
	
	@Column("password")
	private String password;

	/**
	 * Default constructor
	 */
	public LoginEntity() {
		this.userId = 0;
		this.username = "";
		this.password = "";
	}
	/**
	 * Constructor with parameters
	 * @param userId
	 * @param username
	 * @param password
	 */
	public LoginEntity(String username, String password) {
		this.userId = 0;
		this.username = username;
		this.password = password;
	}
	public LoginEntity(int userId, String username, String password) {
		this.userId = userId;
		this.username = username;
		this.password = password;
	}
	/****************************************** GETTERS AND SETTERS ******************************************/
	/**
	 *  Getter for the userId
	 * @return the userId
	 */
	public int getUserId() {return userId;}									
	/**
	 * Setter for the userId
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {this.userId = userId;}					

	/**
	 * Getter for the username
	 * @return the username
	 */
	public String getUsername() {return username;}								
	/**
	 * Setter for the username
	 * @param username the username to set
	 */
	public void setUsername(String username) {this.username = username;}		

	/**
	 * Getter for the password
	 * @return the password
	 */
	public String getPassword() {return password;}								
	/**
	 * Setter for the password
	 * @param password the password to set
	 */
	public void setPassword(String password) {this.password = password;}		

}
