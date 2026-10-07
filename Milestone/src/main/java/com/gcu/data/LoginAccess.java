/**
 * LoginAccess.java 
 */
package com.gcu.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.gcu.data.entity.LoginEntity;
import com.gcu.data.repository.LoginRepository;
import com.gcu.utilities.CSVLogger;

/**
 * LoginAccess
 * This class implements the LoginAccessInterface and provides methods for accessing the login data from the database.
 * It is used by the LoginController class.
 * @param <T> 
 */
@Service
@Primary
public class LoginAccess implements LoginAccessInterface<LoginEntity> {
	@Autowired
	private LoginRepository loginRepository;					

	/**
	 * Constructor
	 * @param loginRepository
	 * @param dataSource
	 */
	public LoginAccess(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;					
	}
	
	/**
	 * Get login information by ID
	 * @return a list of LoginEntity objects
	 */
	@Override
	public LoginEntity getById(LoginEntity t) {
		return loginRepository.findByUserId(t.getUserId());		
	}
	
	/**
	 * Get a user by ID
	 * @return a list of LoginEntity objects
	 */
	@Override
	public LoginEntity getUserLoginById(int t) {
		return loginRepository.findByUserId(t);					
	}
	
	/**
	 * Create a new user
	 * @param t
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
		            "CREATE_USER_ERROR",
		            "Exception while creating user '" + t.getUsername() + "'",
		            e.getMessage()
		        );			
		} 
		return false;											
	}
	
	/**
	 * Get the user ID by username
	 * @param name
	 * @return the user ID
	 */
	@Override
	public int getIdByUsername(String name) {
		
		// Guard against invalid username input
	    if (name == null || name.isEmpty()) {
	        CSVLogger.log(
	            null,
	            "DATA",
	            "GET_ID_BY_USERNAME_ERROR",
	            "Username was null or empty",
	            null
	        );

	        return 0;
	    }

	    LoginEntity user = loginRepository.findByUsername(name);

	    // User was not found
	    if (user == null) {
	        CSVLogger.log(
	            null,
	            "DATA",
	            "GET_ID_BY_USERNAME_NOT_FOUND",
	            "No user found for username: " + name,
	            null
	        );

	        return 0;
	    }

	    return user.getUserId();											
	}

	/**
	* Checks a users role by their ID
	* 
	* @param userId The ID of the user whose role is to be checked
	* @return A list of role names associated with the user
	*/
	public List<String> findRoleIdByUserId(int userId) {
		List<String> roleName = new ArrayList<String>();								
		int role = loginRepository.findRoleIdByUserId(userId);							

		// map numeric role ID to Spring Security role name
		if(role == 1) {																	
			roleName.add("ROLE_admin");													
		} else if(role == 2) {															
			roleName.add("ROLE_user");													
		} else {
			CSVLogger.log(
	                userId,
	                "DATA",
	                "FIND_ROLE_ERROR",
	                "Unknown role id '" + role + "' for userId " + userId,
	                null
	            );
		}
		return roleName;																
	}
	
	/**
	 * Get a user by username
	 * @param username
	 * @return the user
	 */
	public LoginEntity findByUsername(String username) {
		return loginRepository.findByUsername(username);								
	}
	
	/**
	 * Get a username by user ID
	 * @param userId ID of the user whose username is to be retrieved
	 * @return the username the username of the user with the specified ID
	 */
	public String getUsernameById(int userId) {
		LoginEntity user = loginRepository.findByUserId(userId);						
		if (user != null) {																
			return user.getUsername();													
		} else {
			CSVLogger.log(
	                userId,
	                "DATA",
	                "GET_USERNAME_BY_ID_ERROR",
	                "No user found with userId " + userId,
	                null
	            );
			return null;																	
		}
	}
}