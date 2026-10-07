/**
 * UserAccountService.java
 * Service class for handling user account operations
 * such as fetching and updating user information
 */
package com.gcu.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gcu.data.LoginAccess;
import com.gcu.model.RegistrationModel;
import com.gcu.model.UserModel;
import com.gcu.utilities.CSVLogger;

@Service
public class UserAccountService implements UserAccountServiceInterface {

	 @Autowired
	 private LoginAccess loginAccess;

	 @Autowired
	 private RegistrationService regService;

	 /**
	  * Returns the currently authenticated user's account info
	  * @return RegistrationModel containing the user's account information
	  */
	 public RegistrationModel getCurrentUserAccount() {
	     try {
	     String username = getAuthenticatedUsername();
	     int userId = loginAccess.getIdByUsername(username);

	     RegistrationModel user = regService.getUserById(userId);

	     // log if no user record exists for resolved ID
	     if (user == null) {
	         CSVLogger.log(
                     null,
                     "SERVICE",
                     "ACCOUNT_FETCH_FAILED",
                     "No user found for ID " + userId,
                     ""
             );
	     }
	        
	     return user;
	     } catch (Exception e) {
	     	CSVLogger.log(
	                 null,
	                 "SERVICE",
	                 "ACCOUNT_FETCH_ERROR",
	                 "Failed to fetch current user account",
	                 e.getMessage()
	         );
	     	return null;
	     }
	 }

	 /**
	  * Updates the currently authenticated user's account info
	  * @param user The updated user information
	  * @return true if update was successful, false otherwise
	  */
	 public boolean updateCurrentUser(UserModel user) {
	    try {
	    	String username = getAuthenticatedUsername();
	    	int userId = loginAccess.getIdByUsername(username);

	    	// ensure update applies to authenticated user only
	        user.setUserId(userId);

	        boolean updated = regService.updateUserInfo(user);

	        CSVLogger.log(
	                userId,
	                "SERVICE",
	                updated ? "ACCOUNT_UPDATE_SUCCESS" : "ACCOUNT_UPDATE_FAILED",
	                updated
	                    ? "Updated account info for userId " + userId
	                    : "Failed to update account info for userId " + userId,
	                ""
	        );

	        return updated;
	    } catch (Exception e) {
	    	CSVLogger.log(
	                 null,
	                 "SERVICE",
	                 "ACCOUNT_UPDATE_ERROR",
	                 "Exception while updating account info",
	                 e.getMessage()
	        );
	        return false;
	    }
	 }
	 
	 /**
	  * Helper: gets the username of the authenticated user
	  * @return the username of the authenticated user
	  */
	 public String getAuthenticatedUsername() {
	     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     return auth.getName();
	 }
}