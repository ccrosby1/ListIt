/**
 * UserAccountServiceInterface.java
 * Interface for UserAccountService to define the contract for fetching and updating user
 */
package com.gcu.business;

import com.gcu.model.RegistrationModel;
import com.gcu.model.UserModel;

public interface UserAccountServiceInterface {

	RegistrationModel getCurrentUserAccount();
	boolean updateCurrentUser(UserModel user);
}