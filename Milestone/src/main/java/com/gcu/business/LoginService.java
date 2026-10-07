/**
 * LoginService.java
 */
package com.gcu.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gcu.data.LoginAccess;
import com.gcu.data.entity.LoginEntity;
import com.gcu.mapper.LoginMapper;
import com.gcu.model.LoginModel;
import com.gcu.model.UserModel;
import com.gcu.utilities.CSVLogger;
/**
 * LoginService class implements the LoginServiceInterface to provide
 * functionality for user login and logout.
 */
@Service
public class LoginService implements LoginServiceInterface, UserDetailsService {	
	@Autowired
	private LoginAccess loginAccess;	
	
	public LoginService(LoginAccess loginAccess) {
		this.loginAccess = loginAccess;
	}
	
	/**
	 * Gets the username by user ID.
	 * @param userId the ID of the user
	 * @return the username of the user
	 */
	@Override
	public String getUserNameById(int userId) {
		try {
			LoginEntity user = loginAccess.getUserLoginById(userId);		
			return user.getUsername();									
		} catch (Exception e) {				
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "LOGIN_GET_USERNAME_ERROR",
	                "Failed to get username for userId " + userId,
	                e.getMessage()
	        );
		}
		return null;														
	}
	/**
	 * Gets a user by username
	 * @param userId the ID of the user
	 * @return
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    LoginEntity user = loginAccess.findByUsername(username);	
	    
	    if (user == null) {		
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "LOGIN_USER_NOT_FOUND",
	                "User not found: '" + username + "'",
	                ""
	        );
	    	
	        throw new UsernameNotFoundException("User not found: " + username);										
	    }

	    int userId = user.getUserId();
	    		
	    // convert role names into Spring Security authorities
	    List<String> roles = loginAccess
	    		.findRoleIdByUserId(userId);						

	    CSVLogger.log(
	    		userId,
	            "SERVICE",
	            "LOGIN_ROLES_LOADED",
	            "Loaded roles for '" + username + "': " + roles,
	            ""
	    );
	    
	    List<GrantedAuthority> authorities = roles.stream()					
	            .map(SimpleGrantedAuthority::new)							
	            .collect(Collectors.toList());								
	    
	    return User.withUsername(user.getUsername())						
	               .password(user.getPassword())							
	               .authorities(authorities)								
	               .build();												
	}
	/**
	 * Gets the user ID by username.
	 * @param username the username of the user
	 * @return the ID of the user
	 */
	@Override
	public int getUserId(String username) {
        return loginAccess.getIdByUsername(username);						
    }
	
	/**
	 * Gets the user by ID.
	 * @param userId the ID of the user
	 * @return the user model
	 */
	@Override
	public UserModel getUserById(int userId) {
		
		return null;
	}
	/**
	 * Gets the user login by id
	 * @param userId
	 * @return
	 */
	@Override
	public LoginModel getUserLoginById(int userId) {
		try {																
			LoginEntity entity = loginAccess.getUserLoginById(userId);	
			
			// map login entity fields into model
			LoginModel model = LoginMapper.toModel(entity); 									
			
			return model; 											
		} catch (Exception e) {				
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "LOGIN_GET_USER_ERROR",
	                "Failed to get login info for userId " + userId,
	                e.getMessage()
	        );
		}
		return null;														
	}

}