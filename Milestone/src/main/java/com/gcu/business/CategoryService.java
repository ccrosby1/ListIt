/**
 * CategoryService.java
 * This class is the catalogService layer for the Category model.
 */
package com.gcu.business;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcu.data.CategoryAccess;
import com.gcu.data.LoginAccess;
import com.gcu.data.entity.CategoryEntity;
import com.gcu.mapper.CategoryMapper;
import com.gcu.model.CategoryModel;
import com.gcu.utilities.CSVLogger;

/**
 * This class provides the business logic for managing categories.
 * It implements the CategoryServiceInterface and provides methods to
 * add, update, delete, and retrieve categories.
 */
@Service
public class CategoryService implements CategoryServiceInterface {
	@Autowired
	private CategoryAccess categoryAccess;	
	@Autowired
	private LoginAccess loginAccess;
	
	/**
	 * This method will return a category by its ID
	 * @param id ID of the category
	 * @return
	 */
	@Override
	public String getCategoryById(int id) {
		CategoryEntity entity = categoryAccess.getById(id);
        return (entity != null) ? entity.getName() : null;
	}
	
	/**
	 * This method will return a category by its ID for a given user ID
	 * @param id ID of the category
	 * @param userId ID of the user
	 * @return
	 */
	@Override
	public CategoryModel getCategoryById(int id, int userId) {
		 CategoryEntity entity = categoryAccess.getById(id);
	        return (entity != null) ? CategoryMapper.toModel(entity) : null;
	}
	
	/**
	 * This method will return a list of categories
	 * @param userId ID of the user to get categories for
	 * @return
	 */
	public Map<Integer, String> getStringCategories(int userId) {
		return categoryAccess.getStringCategories(userId);														
	}
	
	/**
	 * This method will return a list of all categories
	 * @param userId ID of the user to get categories for
	 * @return
	 */
	@Override
	public List<CategoryModel> getAllCategories(int userId) {
		 return CategoryMapper.toModelList(categoryAccess.getAll(userId));																		
	}

	/**
	 * This method will return a list of all parent categories
	 * @param userId ID of the user to get parent categories for
	 * @return
	 */
	@Override
	public List<CategoryModel> getAllParentCategories(int userId) {
		return CategoryMapper.toModelList(categoryAccess.getAllParentCategories(userId));
	}

	/**
	 * This method will return a list of all child categories for a given parent category ID
	 * @param id ID of the parent category
	 * @return
	 */
	@Override
	public List<CategoryModel> getChildren(int id) {
		return CategoryMapper.toModelList(categoryAccess.getChildren(id));
	}

	/**
	 * This method will return a category by its name and user ID
	 * @param parentString Name of the category
	 * @param userId ID of the user
	 * @return
	 */
	@Override
	public CategoryModel getByNameAndUserId(String parentString, int userId) {
		CategoryEntity entity = categoryAccess.getByNameAndUserId(parentString, userId);
        return (entity != null) ? CategoryMapper.toModel(entity) : null;
	}

	/**
	 * This method will add a new category for a given user ID and parent category ID
	 * @param category Name of the category to add
	 * @param userid ID of the user to add the category for
	 * @param parentId ID of the parent category
	 * @return
	 */
	@Override
	public boolean addCategory(CategoryModel category) {
		// persist new category for user
		boolean created = categoryAccess.addCategory(
	            category.getName(),
	            category.getParentId(),
	            category.getUserId()
	    );

		CSVLogger.log(
	            category.getUserId(),
	            "SERVICE",
	            created ? "CATEGORY_CREATE_SUCCESS" : "CATEGORY_CREATE_FAILED",
	            created
	                ? "Created category '" + category.getName() + "'"
	                : "Failed to create category '" + category.getName() + "'",
	            ""
	    );

	    return created;
	}

	/**
	 * This method will update a category by its ID
	 * @param id ID of category to update
	 * @param category New name of category
	 * @return
	 */
	@Override
	public String updateCategory(int id, String category) {
		categoryAccess.updateNameById(id, category);
		CSVLogger.log(
	            null,
	            "SERVICE",
	            "CATEGORY_UPDATE_NAME",
	            "Updated category ID " + id + " to '" + category + "'",
	            ""
	    );
        return category;
	}

	/**
	 * This method will update the parent of a category
	 * @param id ID of category to update
	 * @param parentId ID of new parent category
	 * @return
	 */
	@Override
	public String updateCategoryParent(int id, int parentId) {
		CategoryEntity entity = categoryAccess.getById(id);
		
		// ensure category exists before updating parent
		if (entity == null) {
        	CSVLogger.log(
                    null,
                    "SERVICE",
                    "CATEGORY_UPDATE_PARENT_FAILED",
                    "Category ID " + id + " not found",
                    ""
            );
            return null;
        }

        entity.setParentId(parentId);
        categoryAccess.update(entity);

        CSVLogger.log(
                entity.getUserId(),
                "SERVICE",
                "CATEGORY_UPDATE_PARENT",
                "Updated parent of category '" + entity.getName() + "' to ID " + parentId,
                ""
        );
        
        return entity.getName();
	}

	/**
	 * This method will delete a category for a given user ID
	 * @param categoryToDelete ID of category to delete
	 * @param userId ID of category owner
	 * @return
	 */
	@Override
	public boolean deleteCategory(int categoryId, int userId) {
		boolean deleted = categoryAccess.delete(categoryId, userId);

	    CSVLogger.log(
	            userId,
	            "SERVICE",
	            deleted ? "CATEGORY_DELETE_SUCCESS" : "CATEGORY_DELETE_FAILED",
	            (deleted
	                    ? "Deleted category ID " + categoryId
	                    : "Failed to delete category ID " + categoryId),
	            ""
	    );

	    return deleted;
	}
	
	/**
	 * This method will return the category name for a given category ID
	 * @param categoryId ID of category to get name for
	 * @return name of category or null if not found
	 */
	public String getCategoryNameById(int categoryId) {
		CategoryEntity entity = categoryAccess.getById(categoryId);
		return (entity != null) ? entity.getName() : null;
	}
	
	/**
	 * This method will resolve the category ID based on the username and search box input
	 * @param username
	 * @param searchBox
	 * @return
	 */
	public int resolveCategoryId(String username, String searchBox) {
		int userId = loginAccess.getIdByUsername(username);

	    if (searchBox == null || searchBox.isBlank()) {
	        return 1; // default category
	    }

	    // extract last segment as category name
	    String[] parts = searchBox.split("/");
	    String categoryName = parts[parts.length - 1];

	    if (categoryName == null || categoryName.isBlank()) {
	        return 1;
	    }

	    CategoryModel category = getByNameAndUserId(categoryName, userId);
	    return (category != null) ? category.getId() : 1;
	}
}