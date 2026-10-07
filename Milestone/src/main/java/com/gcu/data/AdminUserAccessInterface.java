/**
 * AdminUserAccessInterface.java
 */
package com.gcu.data;

import java.util.List;

import com.gcu.data.entity.AdminUserEntity;

/**
 * Interface for AdminUser data access operations
 * @param <T> The type of the entity
 */
public interface AdminUserAccessInterface <T> {
	
	public boolean update(T adminUser);
	public boolean delete(int id);
	public AdminUserEntity findById(int id);
	public List<T> findAll();
}