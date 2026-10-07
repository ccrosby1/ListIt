/**
 * RegistrationServiceInterface
 * This interface defines the contract for the registration catalogService.
 */
package com.gcu.business;

import com.gcu.model.RegistrationModel;
import com.gcu.model.RegistrationResult;
import com.gcu.model.UserModel;

/**
 * This interface defines the contract for the registration catalogService.
 */
public interface RegistrationServiceInterface {
	
	public boolean registerUser(RegistrationModel registration);	
	public boolean checkUserExists(RegistrationModel registration); 
	public boolean checkEmailExists(RegistrationModel registration); 
	public RegistrationModel getUserById(int userId); 
	public boolean updateUser(RegistrationModel registration); 
	RegistrationResult register(RegistrationModel registration);
	public boolean updateUserInfo(UserModel user);

}
