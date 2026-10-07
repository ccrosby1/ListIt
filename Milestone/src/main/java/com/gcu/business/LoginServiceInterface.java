/**
 * LoginServiceInterface.java
 */
package com.gcu.business;

import com.gcu.model.LoginModel;
import com.gcu.model.UserModel;

/**
 * This interface defines the methods for the LoginService
 */
public interface LoginServiceInterface {

	public UserModel getUserById(int userId);
	public String getUserNameById(int userId);
	public LoginModel getUserLoginById(int userId);
	public int getUserId(String username);
}
