/**
 * RegistrationService
 * This class will handle the registration catalogService for the application.
 * It implements the RegistrationServiceInterface and provides the logic for registering a new user.
 */
package com.gcu.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gcu.data.RegistrationAccess;
import com.gcu.data.entity.UserEntity;
import com.gcu.mapper.RegistrationMapper;
import com.gcu.mapper.UserMapper;
import com.gcu.model.CategoryModel;
import com.gcu.model.LoginModel;
import com.gcu.model.RegistrationModel;
import com.gcu.model.RegistrationResult;
import com.gcu.model.UserModel;
import com.gcu.utilities.CSVLogger;
import com.gcu.utilities.Utilities;

/**
 * This class provides the business logic for the registration catalogService.
 * It handles user registration, checking if a user or email already exists,
 * updating user information, and retrieving user details.
 */
@Service
public class RegistrationService implements RegistrationServiceInterface {
	@Autowired
	private LoginService loginService; 	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RegistrationAccess registrationAccess;		 	
	
	/**
	 * Register a new user
	 * @param registration
	 * @return
	 */
	@Transactional
	@Override
	public boolean registerUser(RegistrationModel registration) {
		String time = Utilities.getCurrentTime(); 				
		
		try {
			boolean userCreated = registrationAccess
							.registerUser(registration.getUsername(), 
							new BCryptPasswordEncoder().encode(registration.getPassword()), 
							registration.getFirstName(), registration.getLastName(), 
							registration.getEmail(), registration.getPhone(), time, time);
							
			CSVLogger.log(
		            registration.getUserId(),
		            "SERVICE",
		            userCreated ? "REGISTER_USER" : "REGISTER_USER_FAILED",
		            userCreated
		                ? "User '" + registration.getUsername() + "' registered successfully"
		                : "User '" + registration.getUsername() + "' failed to register",
		            ""
		    );
			
			if (!userCreated) {
			    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			    return false;
			}
			
			int newUserId = registrationAccess.getIdByUsername(registration.getUsername());

			// create a starter category for every new user
			CategoryModel defaultCategory = new CategoryModel(0, newUserId, "Default", 0);

			boolean categoryCreated = categoryService.addCategory(defaultCategory);

			CSVLogger.log(
	                newUserId,
	                "SERVICE",
	                categoryCreated ? "DEFAULT_CATEGORY_CREATED" : "DEFAULT_CATEGORY_FAILED",
	                categoryCreated
	                        ? "Created default category for new user '" + registration.getUsername() + "'"
	                        : "Failed to create default category for new user '" + registration.getUsername() + "'",
	                ""
	        );

			if (!categoryCreated) {
				// rollback entire registration if default category fails
			    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
		    return categoryCreated;

		} catch (Exception e) {	
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "REGISTER_USER_ERROR",
	                "Exception while registering user '" + registration.getUsername() + "'",
	                e.getMessage()
	        );			
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return false;															
		}
	}
	
	/**
	 * Check if the user already exists
	 * @param registration
	 * @return
	 */
	@Override
	public boolean checkUserExists(RegistrationModel registration) {
		return registrationAccess.findByUsername(registration.getUsername()); 	
	}

	/**
	 * Check if the email already exists
	 * @param registration
	 * @return
	 */
	@Override
	public boolean checkEmailExists(RegistrationModel registration) {
		return registrationAccess.findByEmail(registration.getEmail()); 		
	}

	/**
	 * 	Update all fields of the user
	 * @param registration
	 * @return
	 */
	@Override
	public boolean updateUser(RegistrationModel registration) {
		String time = Utilities.getCurrentTime(); 									
		
		try {
			boolean updated = registrationAccess.updateUser(
		            registration.getUsername(),
		            new BCryptPasswordEncoder().encode(registration.getPassword()),
		            registration.getFirstName(),
		            registration.getLastName(),
		            registration.getEmail(),
		            registration.getPhone(),
		            time
		    );
				
			CSVLogger.log(
		            registration.getUserId(),
		            "SERVICE",
		            updated ? "UPDATE_USER" : "UPDATE_USER_FAILED",
		            updated
		                ? "User '" + registration.getUsername() + "' updated successfully"
		                : "User '" + registration.getUsername() + "' failed to update",
		            ""
		    );

		    return updated;
			
		} catch (Exception e) {	
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "UPDATE_USER_ERROR",
	                "Exception while updating user '" + registration.getUsername() + "'",
	                e.getMessage()
	        );
			
			return false;																
		}
		
	}

	/**
	 * Register a new user and return the result
	 * @param registration The registration model containing the user information
	 * @return RegistrationResult object containing status and message
	 */
	@Override
	public RegistrationResult register(RegistrationModel registration) {

	    // Password match check
	    if (!registration.getPassword().equals(registration.getConfirmPassword())) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "REGISTER_VALIDATION_FAILED",
	                "Password mismatch for username '" + registration.getUsername() + "'",
	                ""
	        );
	        return new RegistrationResult(false, "Passwords do not match");
	    }

	    // Username exists?
	    if (checkUserExists(registration)) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "REGISTER_VALIDATION_FAILED",
	                "Username already exists: '" + registration.getUsername() + "'",
	                ""
	        );
	        return new RegistrationResult(false, "Username already exists");
	    }

	    // Email exists?
	    if (checkEmailExists(registration)) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "REGISTER_VALIDATION_FAILED",
	                "Email already exists: '" + registration.getEmail() + "'",
	                ""
	        );
	        return new RegistrationResult(false, "Email already exists");
	    }

	    // attempt full registration (user + default category)
	    boolean created = registerUser(registration);

	    if (!created) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "REGISTER_FAILED",
	                "Registration failed for username '" + registration.getUsername() + "'",
	                ""
	        );
	        return new RegistrationResult(false, "Registration failed");
	    }
	    // Success
	    return new RegistrationResult(true, "Registration successful");
	}
	
	/**
	 * Get user by ID
	 * @param userID
	 * @return
	 */
	@Override
	public RegistrationModel getUserById(int userId) {
		
		try {
			UserEntity userEnt = registrationAccess.getUserById(userId);
			LoginModel loginMod = loginService.getUserLoginById(userId);

			RegistrationModel userFullInfo =
					RegistrationMapper.toModel(userEnt, loginMod);			
		
			
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "GET_USER",
	                "Retrieved user '" + userFullInfo.getUsername() + "'",
	                ""
	        );
			
			return userFullInfo;
			
		} catch (Exception e) {	
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "GET_USER_ERROR",
	                "Failed to retrieve user with ID " + userId,
	                e.getMessage()
	        );
			
			return null;
		}
	}
	
	/**
	 * Update user information
	 * @param user
	 * @return
	 */
	@Override
	public boolean updateUserInfo(UserModel user) {
		UserEntity userEnt = UserMapper.toEntity(user);	
		
		boolean updated = registrationAccess.updateUser(userEnt);
		
		CSVLogger.log(
	            user.getUserId(),
	            "SERVICE",
	            updated ? "UPDATE_USER_INFO" : "UPDATE_USER_INFO_FAILED",
	            updated
	                ? "Updated user info for user ID " + user.getUserId()
	                : "Failed to update user info for user ID " + user.getUserId(),
	            ""
	    );

	    return updated;				
	}
}