/***
 * SharedCatalogController.java
 * 
 * This controller handles requests related to shared catalogs
 * Allowing users to view catalogs shared with them, share their own catalogs, and manage shared permissions
 */
package com.gcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.gcu.business.CatalogService;
import com.gcu.business.ProductService;
import com.gcu.business.SharedCatalogService;
import com.gcu.data.LoginAccess;
import com.gcu.model.CatalogModel;

@Controller
@RequestMapping("/user/shared-inventory")
public class SharedCatalogController {

	@Autowired
	private LoginAccess loginAccess;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private SharedCatalogService sharedCatalogService;
	@Autowired
	private ProductService productService;
	
	/**
	 * View a shared catalog
	 * @param catalogId ID of the catalog to view
	 * @param model Model to pass data to the view
	 * @param auth Authentication object to get the current user
	 * @return The catalog view if authorized, otherwise a 403 error page
	 */
	@GetMapping("/{catalogId}")
	public String viewSharedCatalog(@PathVariable int catalogId,
	                                Model model,
	                                Authentication auth) {

	    String username = auth.getName();
	    int userId = loginAccess.getIdByUsername(username);

	    // check if user has permission to view catalog
	    if (!sharedCatalogService.userCanViewCatalog(userId, catalogId)) {
	        return "error/403";
	    }

	    CatalogModel catalog = catalogService.getCatalogById(catalogId);

	    model.addAttribute("title", catalog.getName() + " (Shared)");
	    model.addAttribute("color", catalog.getColor());
	    model.addAttribute("logid", catalogId);
	    model.addAttribute("products", productService.getAllProductsByCatalog(catalogId));
	    model.addAttribute("userid", userId);
	    model.addAttribute("username", username);
	    model.addAttribute("canEdit", sharedCatalogService.userCanEditCatalog(userId, catalogId));

	    return "catalog";
	}
}
