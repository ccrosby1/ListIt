/**
 * CategoryAccess.java
 * 
 * This class provides access to the category data in the database.
 * It implements the CategoryAccessInterface and provides methods to 
 * add, update, delete, and retrieve categories.
 */
package com.gcu.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gcu.data.entity.CategoryEntity;
import com.gcu.mapper.CategoryMapper;
import com.gcu.utilities.CSVLogger;

/**
 * CategoryAccess class provides access to the category data in the database.
 * It implements the CategoryAccessInterface and provides methods to add, update, delete, and retrieve categories.
 */
@Service
public class CategoryAccess implements CategoryAccessInterface <CategoryEntity> {
	@Autowired
	private ProductAccess productAccess;
	@SuppressWarnings("unused")								
	private DataSource dataSource;							
	private JdbcTemplate jdbcTemplate;						
	
	
	/**
	 * Constructor
	 * 
	 * @param dataSource The DataSource object for database connection.
	 */
	public CategoryAccess(DataSource dataSource) {			
		this.dataSource = dataSource;						
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	/**
	 * This method will return a list of all categories
	 * @return
	 */
	@Override
	public List<CategoryEntity> getAll(int userId) {
		String sql = "SELECT * FROM category WHERE user_id = ?";
		return jdbcTemplate.query(sql, new CategoryMapper(), userId);
	}
	/**
	 * This method will get a category by ID
	 * @param t
	 * @return CategoryEntity category object
	 */
	@Override
	public CategoryEntity getById(int t) {
		String sql = "SELECT * FROM category WHERE category_id = ?";	
		return jdbcTemplate.queryForObject(sql, new CategoryMapper(), t);
	}
	/**
	 * This method will get a category by name
	 * @param name name of the category
	 * @return CategoryEntity category object
	 */
	@Override
	public CategoryEntity getByName(String name) {
		String sql = "SELECT * FROM category WHERE name = ?";
		return jdbcTemplate.queryForObject(sql, new CategoryMapper(), name);
	}
	/**
	 * This method will get a category by name and user ID
	 * @param t
	 * @param userId
	 * @return CategoryEntity category object
	 */
	@Override
	public CategoryEntity getByNameAndUserId(String t, int userId) {
		String sql = "SELECT * FROM category WHERE name = ? AND user_id = ?";	
		return jdbcTemplate.queryForObject(sql, new CategoryMapper(), t, userId);
	}
	/**
	 * This method will get all children of a category
	 * @param parentId
	 * @return List<CategoryModel> list of category model objects
	 */
	@Override
	public List<CategoryEntity> getChildren(int parentId) {
	    String sql = "SELECT * FROM category WHERE parent_id = ?";
	    return jdbcTemplate.query(sql, new CategoryMapper(), parentId);
	}
	/**
	 * This method will update a category
	 * @param t
	 * @return boolean true/false
	 */
	@Override
	public boolean update(CategoryEntity t) {
		String sql = "UPDATE category SET name = ?, parent_id = ?, user_id = ? WHERE category_id = ?";
	    try {
	        jdbcTemplate.update(sql, t.getName(), t.getParentId(), t.getUserId(), t.getCategoryId());
	        return true;
	    } catch (Exception e) {
	    	CSVLogger.log(
	                t.getUserId(),
	                "DATA",
	                "UPDATE_CATEGORY_ERROR",
	                "Exception while updating category '" + t.getName() + "'",
	                e.getMessage()
	            );
	    	return false;
	    }
	}
	
	/**
	 * This method will update a category name by ID
	 * 
	 * @param id ID of category to update
	 * @param cateogry new name of category
	 */
	@Override
	public void updateNameById(int id, String category) {
		String sql = "UPDATE category SET name = ? WHERE category_id = ?";	
		try {
			jdbcTemplate.update(sql, category, id);							
		} catch (Exception e) {
			CSVLogger.log(
		            null,
		            "DATA",
		            "UPDATE_CATEGORY_NAME_ERROR",
		            "Exception while updating category name for ID " + id,
		            e.getMessage()
		        );	
		}
	}
	
	/**
	 * This method will delete a category by ID and user ID
	 * 
	 * @param categoryId ID of category to delete
	 * @param userId ID of category owner
	 * @return boolean true/false based on success or failure
	 */
	@Transactional
	@Override
	public boolean delete(int categoryId, int userId) {
		try {
	        deleteChildCategories(categoryId, userId);
	        
	        jdbcTemplate.update(
	                "DELETE FROM category WHERE category_id = ? AND user_id = ?",
	                categoryId, userId
	            );

	            return true;

	        } catch (Exception e) {
	            CSVLogger.log(
	                userId,
	                "DATA",
	                "DELETE_CATEGORY_ERROR",
	                "Failed deleting category " + categoryId,
	                e.getMessage()
	            );
	            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	            return false;
	        }
	}
	
	/**
	 * This method will  delete all child categories of a given parent category
	 * 
	 * @param parentId ID of parent category
	 * @param userId ID of category owner
	 * @return boolean true/false based on success or failure
	 */
	private void deleteChildCategories(int parentId, int userId) {
		// Get all children
	    List<Integer> childCategoryIds = jdbcTemplate.queryForList(
	            "SELECT category_id FROM category WHERE parent_id = ? AND user_id = ?",
	            Integer.class,
	            parentId,
	            userId
	    );

	    for (Integer childCategoryId : childCategoryIds) {

	        // delete products in this child category
	        List<Integer> productIds = jdbcTemplate.queryForList(
	            "SELECT product_id FROM product WHERE category_category_id = ?",
	            Integer.class,
	            childCategoryId
	        );

	        for (Integer productId : productIds) {
	            productAccess.delete(productId);
	        }

	        // delete the child category itself
	        jdbcTemplate.update(
	            "DELETE FROM category WHERE category_id = ? AND user_id = ?",
	            childCategoryId,
	            userId
	        );
	    }
	}
	
	/**
	 * This method will add a new child category with a parent ID
	 * 
	 * @param category name of category
	 * @param parentId parent ID of category
	 * @param userId user ID of category owner
	 * @return boolean true/false
	 */
	@Override
	public boolean addCategory(String category, int parentId, int userId) {
		String sql = "INSERT INTO category (name, parent_id, user_id) VALUES (?, ?, ?)";

	    try {
	        jdbcTemplate.update(sql, category, parentId, userId);
	        return true;
	    } catch (Exception e) {
	    	CSVLogger.log(
	                userId,
	                "DATA",
	                "ADD_CATEGORY_ERROR",
	                "Exception while adding category '" + category + "'",
	                e.getMessage()
	            );
	    	return false;
	    }
	}
	
	/**
	 * This method will get all parent categories
	 * 
	 * @param userId user ID of the category owner
	 * @return List<CategoryModel> list of category model objects
	 */
	@Override
	public List<CategoryEntity> getAllParentCategories(int userId) {
		String sql = "SELECT * FROM category WHERE user_id = ? AND parent_id = 0";
	    return jdbcTemplate.query(sql, new CategoryMapper(), userId);
	}
	
	/**
	 * This method will get all categories as a string
	 * @param userID
	 * @return 
	 * @return List<String> list of categories
	 */
	@Override
	public Map<Integer, String> getStringCategories(int userID) {
	
		List<CategoryEntity> categories = getAll(userID);	
		Map<Integer, String> fullListings = tree(categories);	
		return fullListings;													
		
	}
	/**
	 * This method will get all categories by parent ID
	 * 
	 * @param parentId
	 * @return List<CategoryEntity> list of category objects
	 */
	@Override
	public List<CategoryEntity> getCategoryByParentId(int parentId) {
		String sql = "SELECT * FROM category WHERE parent_id = ?";
		return jdbcTemplate.query(sql, new CategoryMapper(), parentId);
	}
	/**
	 * This method will create a tree of categories
	 * 
	 * @param categories
	 * @return
	 */
	public Map<Integer, String> tree(List<CategoryEntity> categories) {
	    Map<Integer, String> map = new HashMap<>();

	    // Build lookup
	    Map<Integer, CategoryEntity> lookup = new HashMap<>();
	    for (CategoryEntity c : categories) {
	        lookup.put(c.getId(), c);
	    }

	    for (CategoryEntity c : categories) {
	        String fullPath = buildPath(c, lookup);
	        map.put(c.getId(), fullPath);
	    }

	    return map;
	}

	/**
	 * This method will build the path of a category
	 * 
	 * @param node
	 * @param lookup
	 * @return String path of the category
	 */
	private String buildPath(CategoryEntity node, Map<Integer, CategoryEntity> lookup) {
	    if (node.getParentId() == 0) {
	        return node.getName();
	    }

	    CategoryEntity parent = lookup.get(node.getParentId());
	    if (parent == null) {
	        return node.getName();
	    }

	    return buildPath(parent, lookup) + "/" + node.getName();
	}
	
	/**
	 * This method will get all category IDs by user ID
	 * 
	 * @param userId user ID of the category owner
	 * @return List<Integer> list of category IDs
	 */
	@Override
	public List<Integer> getCategoryIdsByUserId(int userId) {
		return jdbcTemplate.queryForList(
		        "SELECT category_id FROM category WHERE user_id = ?",
		        Integer.class,
		        userId
		    );
	}
}