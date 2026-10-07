/*
 * CatalgShareController.java
 * 
 * This class handles the sharing of catalogs with other users.
 * It provides endpoints to get shared users, share a catalog, and unshare a catalog
 */
package com.gcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gcu.business.SharedCatalogServiceInterface;
import com.gcu.model.SharedCatalogModel;

@Controller
@RequestMapping("/user/catalog/share")
public class CatalogShareController {

    @Autowired
    private SharedCatalogServiceInterface sharedCatalogService;

    /**
	 * Get all users with whom a specific catalog is shared
	 * @param catalogId ID of the catalog
	 * @return List of SharedCatalogModel objects representing the shared users
	 */
    @GetMapping("/{catalogId}")
    @ResponseBody
    public List<SharedCatalogModel> getSharedUsers(@PathVariable int catalogId) {
        return sharedCatalogService.getSharedUsersForCatalog(catalogId);
    }

    /**
     * Share a catalog with a specific user
     * @param catalogId ID of the catalog tobe shared
     * @param username Username of the user to share the catalog with
     * @param permission Permission level for the shared catalog (default to "view')
     * @return "OK" if sharing was successful, "ERROR" otherwise
     */
    @PostMapping("/{catalogId}")
    @ResponseBody
    public String shareCatalog(@PathVariable int catalogId,
                               @RequestParam String username,
                               @RequestParam(defaultValue = "view") String permission) {

        boolean ok = sharedCatalogService.shareCatalogWithUsername(catalogId, username, permission);
        // return simple status string for AJAX handlers
        return ok ? "OK" : "ERROR";
    }

    /**
	 * Unshare a catalog from a specific user
	 * @param catalogId ID of the catalog to be unshared
	 * @param sharedUserId ID of the user to unshare the catalog from
	 * @return "OK" if unsharing was successful, "ERROR" otherwise
	 */
    @DeleteMapping("/{catalogId}/{sharedUserId}")
    @ResponseBody
    public String unshareCatalog(@PathVariable int catalogId,
                                 @PathVariable int sharedUserId) {

        boolean ok = sharedCatalogService.removeShare(catalogId, sharedUserId);
        // return simple status string for AJAX handlers
        return ok ? "OK" : "ERROR";
    }
}