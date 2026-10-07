/**
 * RegistrationInterface.java
 */
package com.gcu.data;

import com.gcu.data.entity.UserEntity;

/**
 * RegistrationInterface
 * This interface defines the methods for accessing the user data from the database.
 * It is used by the UserAccess class.
 * @param <T> 
 */
public interface RegistrationInterface <T> {

	public T getUserById(int t);											
	public boolean updateUser(T t);										
	public boolean deleteUser(T t);										
	boolean updateUser(UserEntity t);
}