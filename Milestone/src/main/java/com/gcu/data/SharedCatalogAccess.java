/**
 * SharedCatalogAccess.java
 * 
 * This class provides methods to access shared catalog data from the database
 */
package com.gcu.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gcu.model.SharedCatalogModel;
import com.gcu.mapper.SharedCatalogMapper;

@Service
public class SharedCatalogAccess implements SharedCatalogAccessInterface{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Constructor for SharedCatalogAccess
	 * @param dataSource Data source for db connection
	 */
    public SharedCatalogAccess(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
	 * Get all catalog IDs shared with a specific user
	 * @param userId ID of the user
	 * @return List of catalog IDs
	 */
    @Override
    public List<Integer> getAllSharedWithUser(int userId) {
        String sql = "SELECT catalog_id FROM catalog_share WHERE shared_user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    /**
     * Check if a user has a specific permission for a catalog
     * @param userId User id to check
     * @param catalogId catalog id to check
     * @param permission permission to check (e.g., "view", "edit")
     * @return true if user has the permission, false otherwisee
     */
    @Override
    public boolean hasPermission(int userId, int catalogId, String permission) {
        String sql = "SELECT COUNT(*) FROM catalog_share WHERE shared_user_id = ? AND catalog_id = ? AND permission = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, catalogId, permission);
        return count != null && count > 0;
    }

    /**
     * Get all shared users for a specific catalog
     * @param catalogId ID of the catalog
     * @return List of SharedCatalogModels representing the shared users
     */
    @Override
    public List<SharedCatalogModel> getSharedUsersForCatalog(int catalogId) {
    	String sql =
                "SELECT cs.id, cs.catalog_id, cs.shared_user_id, cs.permission, cs.created_date, uc.username " +
                "FROM catalog_share cs " +
                "JOIN user_credentials uc ON cs.shared_user_id = uc.user_id " +
                "WHERE cs.catalog_id = ?";

            return jdbcTemplate.query(sql, new SharedCatalogMapper(), catalogId);
    }

    /**
	 * Find a specific shared catalog entry for a user
	 * @param catalogId ID of the catalog
	 * @param sharedUserId ID of the user to check sharing for
	 * @return SharedCatalogModel if found, null otherwise
	 */
    @Override
    public SharedCatalogModel findShare(int catalogId, int sharedUserId) {
    	String sql =
                "SELECT cs.id, cs.catalog_id, cs.shared_user_id, cs.permission, cs.created_date, uc.username " +
                "FROM catalog_share cs " +
                "JOIN user_credentials uc ON cs.shared_user_id = uc.user_id " +
                "WHERE cs.catalog_id = ? AND cs.shared_user_id = ?";

            List<SharedCatalogModel> list = jdbcTemplate.query(sql, new SharedCatalogMapper(), catalogId, sharedUserId);
            return list.isEmpty() ? null : list.get(0);
    }

    /**
	 * Add a new shared catalog entry for a specific user
	 * @param catalogId ID of the catalog
	 * @param sharedUserId ID of the user to share with
	 * @param permission Permission level (e.g., "view", "edit")
	 * @return true if the entry was added, false otherwise
	 */
    @Override
    public boolean addShare(int catalogId, int sharedUserId, String permission) {
    	String sql = "INSERT INTO catalog_share (catalog_id, shared_user_id, permission) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, catalogId, sharedUserId, permission) > 0;
    }
    
    /**
	 * Remove a shared catalog entry for a specific user
	 * @param catalogId ID of the catalog
	 * @param sharedUserId ID of the user to remove sharing for
	 * @return true if the entry was removed, false otherwise
	 */	
    @Override
    public boolean removeShare(int catalogId, int sharedUserId) {
    	String sql = "DELETE FROM catalog_share WHERE catalog_id = ? AND shared_user_id = ?";
        return jdbcTemplate.update(sql, catalogId, sharedUserId) > 0;
    }
}
