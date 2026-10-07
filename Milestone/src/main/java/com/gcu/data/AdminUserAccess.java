/**
 * AdminUserAccess.java
 * 
 * This class provides access to the admin user data in the database.
 * It implements the AdminUserAccessInterface and provides methods to create, update, delete, and find admin users.
 */
package com.gcu.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gcu.data.entity.AdminUserEntity;
import com.gcu.mapper.AdminUserMapper;
import com.gcu.utilities.CSVLogger;

/**
 * AdminUserAccess class provides access to the admin user data in the database.
 * It implements the AdminUserAccessInterface and provides methods to create, update, delete, and find admin users.
 */
@Repository
public class AdminUserAccess implements AdminUserAccessInterface<AdminUserEntity> {
	
	@SuppressWarnings("unused")							
	private DataSource dataSource;							
	private JdbcTemplate jdbcTemplate;					
	
	/**
	 * Constructor
	 * @param adminUserRepository The repository for admin user data access
	 * @param dataSource The DataSource object for database connection
	 */
	public AdminUserAccess(DataSource dataSource) {
		this.dataSource = dataSource;										
		this.jdbcTemplate = new JdbcTemplate(dataSource);					
	}

	/**
	 * Update an existing admin user
	 * @param adminUser The AdminUserEntity object representing the user to update
	 * @return true if the user was updated successfully, false otherwise
	 */
	@Override
	public boolean update(AdminUserEntity adminUser) {
		String sql = 
		        "UPDATE user u " +
		        "JOIN user_roles ur ON u.user_id = ur.user_id " +
		        "SET u.first_name = ?, " +
		        "    u.last_name = ?, " +
		        "    u.email = ?, " +
		        "    u.phone = ?, " +
		        "    u.update_date = ?, " +
		        "    ur.roles_id = ? " +
		        "WHERE u.user_id = ?";

		    try {
		    	int rows = jdbcTemplate.update(sql,
			    		adminUser.getFirstName(),
			    		adminUser.getLastName(),
			    		adminUser.getEmail(),
			    		adminUser.getPhone(),
			    		adminUser.getUpdatedDate(),
			    		adminUser.getRole(),          // roles_id
			    		adminUser.getUser_id());      // WHERE user_id = ?

			    return rows > 0;
		    } catch (Exception e) {
		    	CSVLogger.log(
		                adminUser.getUser_id(),
		                "DATA",
		                "UPDATE_ADMIN_USER_ERROR",
		                "Exception updating admin user ID " + adminUser.getUser_id(),
		                e.getMessage()
		            );

		            return false;
		    }
	}
	/**
	 * Delete a user by ID
	 * @param userId The ID of the user to delete
	 * @return true if the user was deleted successfully, false otherwise
	 */
	@Transactional
	@Override
	public boolean delete(int userId) {
		try {
			jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", userId);
		    jdbcTemplate.update("DELETE FROM user WHERE user_id = ?", userId);
		    jdbcTemplate.update("DELETE FROM user_credentials WHERE user_id = ?", userId);

		    return true;
	    } catch (Exception e) {
	        CSVLogger.log(
	        		userId, "DATA", "DELETE_USER_ERROR",
	            "Failed deleting user " + userId,
	            e.getMessage()
	        );
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        return false;
	    }
	}
	    
	/**
	 * Find all users
	 * @return List of AdminUserEntity objects representing all users
	 */
	@Override
	public List<AdminUserEntity> findAll() {
		String sql =
		        "SELECT " +
		        "   uc.user_id, " +
		        "   uc.username, " +
		        "   uc.password, " +
		        "   u.first_name, " +
		        "   u.last_name, " +
		        "   u.email, " +
		        "   u.phone, " +
		        "   u.created_date, " +
		        "   u.update_date, " +
		        "   ur.roles_id " +
		        "FROM user_credentials uc " +
		        "JOIN user u ON uc.user_id = u.user_id " +
		        "JOIN user_roles ur ON uc.user_id = ur.user_id";

		return jdbcTemplate.query(sql, new AdminUserMapper());																		
	}

	/**
	 * Find a user by ID
	 * @param id The ID of the user to find
	 */
	@Override
	public AdminUserEntity findById(int id) {
		String sql =
		        "SELECT " +
		        "   uc.user_id, " +
		        "   uc.username, " +
		        "   uc.password, " +
		        "   u.first_name, " +
		        "   u.last_name, " +
		        "   u.email, " +
		        "   u.phone, " +
		        "   u.created_date, " +
		        "   u.update_date, " +
		        "   ur.roles_id " +
		        "FROM user_credentials uc " +
		        "JOIN user u ON uc.user_id = u.user_id " +
		        "JOIN user_roles ur ON uc.user_id = ur.user_id " +
		        "WHERE uc.user_id = ?";

		return jdbcTemplate.queryForObject(sql, new AdminUserMapper(), id);																			
	}

}
