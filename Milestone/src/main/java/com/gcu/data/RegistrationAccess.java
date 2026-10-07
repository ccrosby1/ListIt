/**
 * RegistrationAccess.java
 */
package com.gcu.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gcu.data.entity.*;
import com.gcu.data.repository.LoginRepository;
import com.gcu.mapper.UserMapper;
import com.gcu.utilities.CSVLogger;

/**
 * RegistrationAccess.java
 * This class is responsible for accessing the registration data from the database.
 * It implements the LoginAccessInterface and RegistrationInterface interfaces.
 * @param <T>
 */
@Service
public class RegistrationAccess implements LoginAccessInterface<LoginEntity>, RegistrationInterface<UserEntity> {	
	@Autowired
	private LoginRepository loginRepository;				
	@SuppressWarnings("unused")
	private DataSource dataSource;							
	private JdbcTemplate jdbcTemplate;						
	
	/**
	 * Constructor
	 * @param loginRepository
	 * @param userRepository
	 * @param dataSource
	 */
	public RegistrationAccess(LoginRepository loginRepository, DataSource dataSource) {
		this.loginRepository = loginRepository;				
		this.dataSource = dataSource;						
		this.jdbcTemplate = new JdbcTemplate(dataSource);	
	}
	/**
	 * Get a list of all users
	 * @return a list of UserEntity objects
	 */
	public List<UserEntity> getAllUsers() {													
		String sql = "SELECT * FROM user";													
		List<UserEntity> users = jdbcTemplate.query(sql, new UserMapper());		
		return users;																		
	}
	/**
	 * Register a new user
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param createdDate
	 * @param updatedDate
	 * @return true if the user was registered successfully, false otherwise
	 */
	@Transactional
	public boolean registerUser(String username, String password, 
								String firstName, String lastName, 
								String email, String phone, 
								String createdDate, String updatedDate) {
		try {																				
			LoginEntity loginEntity = loginRepository
					.save(new LoginEntity(username, password));								
			int newUserId = loginEntity.getUserId();										
			
			// insert user profile data after creating login record
			String sql1 = "INSERT INTO `user` "
						+ "(`user_id`, `first_name`, `last_name`, "
						+ "`email`, `phone`, `created_date`, `update_date`) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?)";									
			
			// assign default role (2 = user)
			String sql2 = "INSERT INTO user_roles (roles_id, user_id) VALUES (?, ?)";		
					
			jdbcTemplate.update(sql1, newUserId, firstName, lastName, 
								email, phone, createdDate, updatedDate);					
			
			jdbcTemplate.update(sql2, 2, newUserId);										

			return true;																	
		} catch (Exception e) {																
			CSVLogger.log(
	                null,
	                "DATA",
	                "REGISTER_USER_ERROR",
	                "Exception while registering user '" + username + "'",
	                e.getMessage()
	        );
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();			
			return false;																	
		}
	}
	/**
	 * Update an existing user
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param updatedDate
	 * @return true if the user was updated successfully, false otherwise
	 */
	@Transactional
	public boolean updateUser(String username, String password, 
								String firstName, String lastName, 
								String email, String phone, 
								String updatedDate) {
		try {																				
			LoginEntity loginEntity = loginRepository.findByUsername(username);				
			int userId = loginEntity.getUserId();											
			
			// update user profile fields
			String sql1 = "UPDATE `user` "
						+ "SET `first_name` = ?, `last_name` = ?, "
						+ "`email` = ?, `phone` = ?, `update_date` = ? "
						+ "WHERE `user_id` = ?";											
						
			// update login credentials
			String sql2 = "UPDATE user_credentials "
						+ "SET password = ?, first_name = ? "
						+ "WHERE user_id = ?";												
					
			jdbcTemplate.update(sql1, firstName, lastName, email, phone, updatedDate, userId);		
			
			jdbcTemplate.update(sql2, password, username, userId);									

			return true;																	
		} catch (Exception e) {																
			CSVLogger.log(
	                null,
	                "DATA",
	                "UPDATE_USER_ERROR",
	                "Exception while updating user '" + username + "'",
	                e.getMessage()
	        );
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();			
			return false;																	
		}
	}
	/**
	 * Check if the user already exists
	 * @param username
	 * @return true if the user exists, false otherwise
	 */
	public boolean findByUsername(String username) {
		try {																				
			return loginRepository.findByUsername(username) != null;					
		} catch (Exception e) {																
			CSVLogger.log(
	                null,
	                "DATA",
	                "FIND_USERNAME_ERROR",
	                "Exception while checking username '" + username + "'",
	                e.getMessage()
	        );
			return false;																	
		}
	}	
	/**
	 * Check if the email already exists
	 * @param email
	 * @return true if the email exists, false otherwise
	 */
	public boolean findByEmail(String email) {
		try {
			String sql = "SELECT * FROM user WHERE email = ?";									
			List<UserEntity> users = jdbcTemplate.query(sql, new UserMapper(), email);	
			return !users.isEmpty();															
		} catch (Exception e) {																	
			CSVLogger.log(
	                null,
	                "DATA",
	                "FIND_EMAIL_ERROR",
	                "Exception while checking email '" + email + "'",
	                e.getMessage()
	        );
			return false;																		
		}
	}	
	/**
	 * Get a list of all users
	 * @return a list of UserEntity objects
	 */
	@Override
	public boolean create(LoginEntity t) {
		try {																					
			loginRepository.save(t); 	
			return true;																		
		} catch (Exception e) {																	
			CSVLogger.log(
	                null,
	                "DATA",
	                "LOGIN_CREATE_ERROR",
	                "Exception creating login entity for '" + t.getUsername() + "'",
	                e.getMessage()
	        );												
			return false;																		
		} 
	}
	/**
	 * Get a user by ID
	 * @param t user ID
	 */
	@Override
	public UserEntity getUserById(int t) {
		String sql = "SELECT * FROM user WHERE user_id = ?";
		return jdbcTemplate.queryForObject(
                sql,
                new UserMapper(),
                t);																	
	}
	@Override
	public boolean deleteUser(UserEntity t) {
		
		return false;
	}
	
	/**
	 * Get a user ID by their username
	 * @param name username to search by
	 * @return id of the user
	 */
	@Override
	public int getIdByUsername(String name) {
		String sql = "SELECT user_id FROM user_credentials WHERE username = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, name);
	}
	/**
	 * Get a user by ID
	 * @param t
	 */
	@Override
	public LoginEntity getUserLoginById(int t) {
		return loginRepository.findById(t).orElse(null);
	}
	@Override
	public LoginEntity getById(LoginEntity t) {
		
		return null;
	}
	/**
	 * Update a user entity
	 * @param t user entity to update 
	 */
	@Override
	public boolean updateUser(UserEntity t) {
		try {																				
			jdbcTemplate.update("UPDATE user "
								+ "SET first_name = ?, last_name = ?, "
								+ "email = ?, phone = ? "
								+ "WHERE user_id = ?", 
								t.getFirstName(), t.getLastName(), 
								t.getEmail(), t.getPhone(), t.getId());						
			return true;																	
		} catch (Exception e) {																
			CSVLogger.log(
	                null,
	                "DATA",
	                "UPDATE_USER_ENTITY_ERROR",
	                "Error updating user entity ID " + t.getId(),
	                e.getMessage()
	        );
			return false;																	
		}
		
	}
}