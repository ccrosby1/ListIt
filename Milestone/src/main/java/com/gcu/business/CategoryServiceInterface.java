/**
 * CategoryServiceInterface.java
 * This interface is the catalogService layer for the Category model.
 */
package com.gcu.business;

import java.util.List;

import com.gcu.model.CategoryModel;

/**
 * This interface defines the methods for the CategoryService class.
 * It provides methods to get, add, update, and delete categories.
 */
public interface CategoryServiceInterface {

	public List<CategoryModel> getAllCategories(int userId);
	public List<CategoryModel> getAllParentCategories(int userId);
	public String getCategoryById(int id);
	public CategoryModel getCategoryById(int id, int userId);
	public List<CategoryModel> getChildren(int id);
	public CategoryModel getByNameAndUserId(String parentString, int userId);
	public boolean addCategory(CategoryModel category);
	public String updateCategory(int id, String category);
	public String updateCategoryParent(int id, int parentId);
	public boolean deleteCategory(int categoryToDelete, int userId);
	
}