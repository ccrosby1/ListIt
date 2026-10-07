/***
 * SharedCatalogService.java
 * 
 * This service provides methods to manage shared catalogs
 * Including retrieving shared catalogs, validating permissions, and sharing catalogs with other users
 */
package com.gcu.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcu.data.CatalogAccess;
import com.gcu.data.LoginAccess;
import com.gcu.data.SharedCatalogAccess;
import com.gcu.model.CatalogModel;
import com.gcu.model.SharedCatalogModel;
import com.gcu.utilities.CSVLogger;

@Service
public class SharedCatalogService implements SharedCatalogServiceInterface {
	@Autowired
	private SharedCatalogAccess sharedCatalogAccess;
	@Autowired
    private CatalogAccess catalogAccess;
	@Autowired
	private LoginAccess loginAccess;

	/**
	 * Get all catalogs shared with a specific user
	 * @param userId ID of the user
	 * @return List of CatalogModel objects representing the shared catalogs
	 */
	@Override
    public List<CatalogModel> getSharedCatalogs(int userId) {

        List<Integer> sharedIds = sharedCatalogAccess.getAllSharedWithUser(userId);
        List<CatalogModel> models = new ArrayList<>();

        for (int id : sharedIds) {
            CatalogModel model = catalogAccess.getById(id);
         
            // skip nulls in case catalog was deleted but share entry still exists
            if (model != null) {
            	// add username of catalog owner to shared catalogs
            	model.setUsername(loginAccess.getUsernameById(model.getUserId()));
                models.add(model);
            }
        }

        return models;
    }
    
    /**
	 * Check if a user has permission to view a specific catalog
	 * @param userId ID of the user
	 * @param catalogId ID of the catalog
	 * @return true if the user can view the catalog, false otherwise
	 */
	@Override
    public boolean userCanViewCatalog(int userId, int catalogId) {
    	try {
            boolean canView = sharedCatalogAccess.hasPermission(userId, catalogId, "view")
                           || sharedCatalogAccess.hasPermission(userId, catalogId, "edit");

            if (!canView) {
                CSVLogger.log(
                    userId,
                    "SERVICE",
                    "SHARED_CATALOG_VIEW_DENIED",
                    "User attempted to VIEW catalog " + catalogId + " without permission",
                    ""
                );
            }

            return canView;

        } catch (Exception e) {

            CSVLogger.log(
                userId,
                "SERVICE",
                "SHARED_CATALOG_VIEW_ERROR",
                "Error checking VIEW permission for catalog " + catalogId,
                e.getMessage()
            );

            return false;
        }
    }
    
    /**
     * Check if a user has permission to edit a specific catalog
     * @param userId Id of user
     * @param catalogId Id of catalog
     * @return true if user can edit catalog, false otherwise
     */
	@Override
    public boolean userCanEditCatalog(int userId, int catalogId) {
    	try {
            boolean canEdit = sharedCatalogAccess.hasPermission(userId, catalogId, "edit");

            if (!canEdit) {
                CSVLogger.log(
                    userId,
                    "SERVICE",
                    "SHARED_CATALOG_EDIT_DENIED",
                    "User attempted to EDIT catalog " + catalogId + " without permission",
                    ""
                );
            }

            return canEdit;

        } catch (Exception e) {

            CSVLogger.log(
                userId,
                "SERVICE",
                "SHARED_CATALOG_EDIT_ERROR",
                "Error checking EDIT permission for catalog " + catalogId,
                e.getMessage()
            );

            return false;
        }
    }
	
	/**
	 * Retrieves the count of shared catalogs for a given user ID.
	 * 
	 * @param userId The ID of the user whose shared catalogs are to be counted.
	 * @return The count of shared catalogs for the given user ID.
	 */
	public int sharedCatalogCountByUserId(int userId) {
		try {
	       
			int count = catalogAccess.countSharedCatalogs(userId);
	        return count;

	    } catch (Exception e) {

	        CSVLogger.log(
	            userId,
	            "SERVICE",
	            "SHARED_CATALOG_COUNT_ERROR",
	            "Error retrieving shared catalog count for user " + userId,
	            e.getMessage()
	        );

	        return 0;
	    }
	}
	
	/**
	 * Retrieves a list of users with whom a specific catalog is shared.
	 *
	 * @param catalogId The ID of the catalog for which to retrieve shared users.
	 * @return A list of SharedCatalogModel objects representing the users with whom the catalog is shared.
	 */
	@Override
    public List<SharedCatalogModel> getSharedUsersForCatalog(int catalogId) {
        return sharedCatalogAccess.getSharedUsersForCatalog(catalogId);
    }
	
	/**
	 * Shares a catalog with a specific user by username and assigns a permission level.
	 *
	 * @param catalogId The ID of the catalog to share.
	 * @param username The username of the user to share the catalog with.
	 * @param permission The permission level to assign (e.g., "view", "edit").
	 * @return true if the catalog was successfully shared, false otherwise.
	 */
	@Override
    public boolean shareCatalogWithUsername(int catalogId, String username, String permission) {

        int sharedUserId = loginAccess.getIdByUsername(username);

        if (sharedUserId == 0) {
            CSVLogger.log(
                null, "SERVICE", "SHARED_CATALOG_SHARE_USER_NOT_FOUND",
                "Attempted to share catalog " + catalogId + " with unknown username: " + username,
                ""
            );
            return false;
        }

        try {
            SharedCatalogModel existing =
                sharedCatalogAccess.findShare(catalogId, sharedUserId);

            // prevent duplicate share entries
            if (existing != null) {
                CSVLogger.log(
                    sharedUserId, "SERVICE", "SHARED_CATALOG_SHARE_DUPLICATE",
                    "Catalog " + catalogId + " already shared with user " + username,
                    ""
                );
                return false;
            }

            boolean ok = sharedCatalogAccess.addShare(
                catalogId, sharedUserId,
                (permission != null ? permission : "view")
            );

            CSVLogger.log(
                sharedUserId, "SERVICE",
                ok ? "SHARED_CATALOG_SHARE_SUCCESS" : "SHARED_CATALOG_SHARE_FAILED",
                ok
                    ? "Shared catalog " + catalogId + " with user " + username
                    : "Failed to share catalog " + catalogId + " with user " + username,
                ""
            );

            return ok;

        } catch (Exception e) {

            CSVLogger.log(
                sharedUserId, "SERVICE", "SHARED_CATALOG_SHARE_ERROR",
                "Error sharing catalog " + catalogId + " with user " + username,
                e.getMessage()
            );

            return false;
        }
    }
	
	/**
	 * Removes a shared catalog for a specific user.
	 *
	 * @param catalogId The ID of the catalog to unshare.
	 * @param sharedUserId The ID of the user to remove the share from.
	 * @return true if the share was successfully removed, false otherwise.
	 */
	@Override
    public boolean removeShare(int catalogId, int sharedUserId) {

        try {
            boolean ok = sharedCatalogAccess.removeShare(catalogId, sharedUserId);

            // log success or failure of unsharing operation
            CSVLogger.log(
                sharedUserId, "SERVICE",
                ok ? "SHARED_CATALOG_UNSHARE_SUCCESS" : "SHARED_CATALOG_UNSHARE_FAILED",
                ok
                    ? "Removed share for catalog " + catalogId + " and userId " + sharedUserId
                    : "Failed to remove share for catalog " + catalogId + " and userId " + sharedUserId,
                ""
            );

            return ok;

        } catch (Exception e) {

            CSVLogger.log(
                sharedUserId, "SERVICE", "SHARED_CATALOG_UNSHARE_ERROR",
                "Error removing share for catalog " + catalogId + " and userId " + sharedUserId,
                e.getMessage()
            );

            return false;
        }
    }
}