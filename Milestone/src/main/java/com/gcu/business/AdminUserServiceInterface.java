/**
 * AdminUserServiceInterface.java
 * Interface for AdminUserService
 */
package com.gcu.business;

import java.util.List;

import com.gcu.model.UserModel;

/**
 * Interface for AdminUserService
 * Provides methods to manage admin users
 */
public interface AdminUserServiceInterface  {

	public List<UserModel> findAllUsers();
	public UserModel findById(int id);
	public boolean deleteUser(int id);
	UserModel getUserForEdit(int userId);
	boolean deleteUserByAdmin(int userId);
	boolean updateUser(int userId, UserModel user);
}
