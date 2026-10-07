/**
 * CategoryAccessInterface.java
 */
package com.gcu.data;

import java.util.List;
import java.util.Map;

import com.gcu.data.entity.CategoryEntity;

/**
 * Interface for accessing category data in the database.
 * Provides methods for CRUD operations on categories.
 * 
 */
public interface CategoryAccessInterface <T> {

	public List<T> getAll(int userId);																
	public CategoryEntity getById(int t);														
	public T getByName(String t);													
	public CategoryEntity getByNameAndUserId(String t, int userId);								
	public List<CategoryEntity> getChildren(int id);										
	public boolean update(T t);														
	public boolean delete(int categoryId, int userId);											
	public boolean addCategory(String category, int parentId, int userId);			
	public List<CategoryEntity> getCategoryByParentId(int parentId);								
	public Map<Integer, String> getStringCategories(int userID);					
	public List<CategoryEntity> getAllParentCategories(int userId);					
	public void updateNameById(int id, String category);
	public List<Integer> getCategoryIdsByUserId(int userId);

}