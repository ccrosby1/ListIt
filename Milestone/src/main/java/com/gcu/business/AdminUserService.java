/**
 * AdminUserService.java
 * This class provides the business logic for managing all users.
 */
package com.gcu.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gcu.data.AdminUserAccess;
import com.gcu.data.CatalogAccess;
import com.gcu.data.CategoryAccess;
import com.gcu.data.LoginAccess;
import com.gcu.data.entity.AdminUserEntity;
import com.gcu.model.UserModel;
import com.gcu.utilities.CSVLogger;
import com.gcu.utilities.Utilities;

/**
 * AdminUserService class implements the AdminUserServiceInterface and provides methods to manage users.
 * It interacts with the AdminUserAccess data access layer to perform CRUD operations on user data.
 */
@Service
public class AdminUserService implements AdminUserServiceInterface {
	@Autowired
    private AdminUserAccess adminUserAccess;
    @Autowired
    private LoginAccess loginAccess;
    @Autowired
    private CatalogAccess catalogAccess;
    @Autowired
    private CategoryAccess categoryAccess;
    	
	/**
	 * Find a user by ID
	 * @param id The ID of the user to find
	 * @return RegistrationModel object representing the user with the given ID
	 */
	@Override
	public UserModel findById(int id) {
		try {
	        AdminUserEntity user = adminUserAccess.findById(id);

	        CSVLogger.log(
	                null,
	                "SERVICE",
	                user != null ? "ADMIN_FIND_USER_SUCCESS" : "ADMIN_FIND_USER_FAILED",
	                user != null
	                    ? "Retrieved user with ID " + id
	                    : "User not found with ID " + id,
	                ""
	        );

	        return (user != null) ? convertEntityToUserModel(user) : null;

	    } catch (Exception e) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "ADMIN_FIND_USER_ERROR",
	                "Exception retrieving user with ID " + id,
	                e.getMessage()
	        );
	        return null;
	    }																		
	}
	
	/**
	 * Find all users
	 * @return List of RegistrationModel objects representing all users in db
	 */
	@Override
    public List<UserModel> findAllUsers() {
        try {
            List<AdminUserEntity> users = adminUserAccess.findAll();
            List<UserModel> models = new ArrayList<>();

            // convert entity into UserModel for UI/admin use
            for (AdminUserEntity user : users) {
                models.add(convertEntityToUserModel(user));
            }

            CSVLogger.log(
                    null,
                    "SERVICE",
                    "ADMIN_FIND_ALL_USERS",
                    "Retrieved " + models.size() + " users",
                    ""
            );
            
            return models;

        } catch (Exception e) {
        	CSVLogger.log(
                    null,
                    "SERVICE",
                    "ADMIN_FIND_ALL_USERS_ERROR",
                    "Exception retrieving all users",
                    e.getMessage()
            );
        	return null;
        }
    }
	
	/**
	 * Get a user for editing by admin
	 * @param userId ID of user to retrieve for editing
	 * @return RegistrationModel object representing the user to edit
	 */
	@Override
	public UserModel getUserForEdit(int userId) {
		try {
	        AdminUserEntity entity = adminUserAccess.findById(userId);

	        CSVLogger.log(
	                null,
	                "SERVICE",
	                entity != null ? "ADMIN_LOAD_USER_FOR_EDIT" : "ADMIN_LOAD_USER_FOR_EDIT_FAILED",
	                entity != null
	                    ? "Loaded user for edit: " + userId
	                    : "User not found for edit: " + userId,
	                ""
	        );

	        return (entity != null) ? convertEntityToUserModel(entity) : null;

	    } catch (Exception e) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "ADMIN_LOAD_USER_FOR_EDIT_ERROR",
	                "Exception retrieving user for edit: " + userId,
	                e.getMessage()
	        );
	    	return null;
	    }
    }
	
	/**
	 * Delete a user by admin, ensuring main admin cannot delete themselves
	 * @param userId ID of user to delete
	 * @return true if user was deleted successfully, false otherwise
	 */
	@Override
    public boolean deleteUserByAdmin(int userId) {

        int adminId = getAuthenticatedAdminId();

        if (userId == adminId) {
            CSVLogger.log(
                    null,
                    "SERVICE",
                    "ADMIN_DELETE_SELF_BLOCKED",
                    "Admin attempted to delete themselves (ID " + adminId + ")",
                    ""
            );
            return false;
        }

        return deleteUser(userId);
    }
	
	/**
	 * Delete a user by id
	 * @param userId ID of user to delete
	 * @return true if user was deleted successfully, false otherwise
	 */
	@Transactional
	@Override
    public boolean deleteUser(int userId) {
		try {
	        // delete all catalogs
	        List<Integer> catalogIds = catalogAccess.getCatalogIdsByUserId(userId);
	        for (Integer catalogId : catalogIds) {
	            catalogAccess.delete(catalogId);
	        }

	        // delete all categories
	        List<Integer> categoryIds = categoryAccess.getCategoryIdsByUserId(userId);
	        for (Integer categoryId : categoryIds) {
	            categoryAccess.delete(categoryId, userId);
	        }

	        // delete user tables (child → parent)
	        adminUserAccess.delete(userId);

	        CSVLogger.log(
	            null,
	            "SERVICE",
	            "ADMIN_DELETE_USER_SUCCESS",
	            "Deleted user with ID " + userId,
	            ""
	        );

	        return true;

	    } catch (Exception e) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "ADMIN_DELETE_USER_ERROR",
	                "Exception deleting user with ID " + userId,
	                e.getMessage()
	        );
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    	return false;
	    }
    }
	
	/**
	 * Update a user's information
	 * @param userId ID of user to update
	 * @param user UserModel object containing updated user information
	 * @return true if user was updated successfully, false otherwise
	 */
	@Override
	public boolean updateUser(int userId, UserModel user) {
		try {
	        // load existing entity
	        AdminUserEntity entity = adminUserAccess.findById(userId);

	        if (entity == null) {
	            CSVLogger.log(
	                    null,
	                    "SERVICE",
	                    "ADMIN_UPDATE_USER_NOT_FOUND",
	                    "User not found for update: " + userId,
	                    ""
	            );
	            return false;
	        }

	        // update fields
	        entity.setFirstName(user.getFirstName());
	        entity.setLastName(user.getLastName());
	        entity.setEmail(user.getEmail());
	        entity.setPhone(user.getPhone());
	        entity.setRole(user.getRole()); 
	        entity.setUpdatedDate(Utilities.getCurrentTime());

	        // persist changes
	        boolean updated = adminUserAccess.update(entity);

	        CSVLogger.log(
	                null,
	                "SERVICE",
	                updated ? "ADMIN_UPDATE_USER_SUCCESS" : "ADMIN_UPDATE_USER_FAILED",
	                updated
	                    ? "Updated user with ID " + userId
	                    : "Failed to update user with ID " + userId,
	                ""
	        );

	        return updated;

	    } catch (Exception e) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "ADMIN_UPDATE_USER_ERROR",
	                "Exception updating user with ID " + userId,
	                e.getMessage()
	        );
	        return false;
	    }
	}
	
	/**
	 * Helper: convert AdminUserEntity to UserModel
	 * @param user AdminUserEntity object to convert
	 * @return UserModel object representing given AdminUserEntity
	 */
	private UserModel convertEntityToUserModel(AdminUserEntity user) {
        UserModel model = new UserModel();
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setEmail(user.getEmail());
        model.setPhone(user.getPhone());
        model.setCreatedDate(user.getCreatedDate());
        model.setUpdatedDate(user.getUpdatedDate());
        model.setRole(user.getRole());
        model.setUserId(user.getUser_id());
        model.setUsername(user.getUsername());
        return model;
    }
	
	/**
	 * Helper: get authenticated admin's username
	 * @return username of authenticated admin
	 */
    public String getAuthenticatedUsername() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return auth.getName();
    }
	
	/**
	 * Helper: get authenticated admin's user ID
	 * @return user ID of authenticated admin
	 */
	public int getAuthenticatedAdminId() {
        String username = getAuthenticatedUsername();
        return loginAccess.getIdByUsername(username);
    }
	
	/**
	 * Helper: get authenticated admin's display info
	 * @return AdminInfo object containing username and ID of admin
	 */
	public AdminInfo getAdminDisplayInfo() {
		String username = getAuthenticatedUsername();

	    return new AdminInfo(username, getAuthenticatedAdminId());
    }
		
	/**
	* Simple DTO class to hold admin display info
	*/
	public static class AdminInfo {
	    public String username;
	    public int adminId;

	    /**
	     * Constructor for AdminInfo
	     * @param username Admin's username
	     * @param adminId Admin's user ID
	     */
	    public AdminInfo(String username, int adminId) {
	        this.username = username;
	        this.adminId = adminId;
	    }
	}
}