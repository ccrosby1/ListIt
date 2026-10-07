/**
 * CatalogController.java
 * This class is the controller for the catalog page.
 * It handles the requests for the catalog page and returns the appropriate view.
 */
package com.gcu.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.CatalogService;
import com.gcu.business.ProductService;
import com.gcu.business.SharedCatalogService;
import com.gcu.business.StorageServiceInterface;
import com.gcu.utilities.Utilities;
import com.gcu.data.LoginAccess;
import com.gcu.model.CatalogModel;
import com.gcu.model.SharedCatalogModel;

import jakarta.validation.Valid;

/**
 * This class is the controller for the catalog page.
 * It handles the requests for the catalog page and returns the appropriate view.
 */
@Controller
@RequestMapping("/user/catalog") 									
public class CatalogController {
	@Autowired
	LoginAccess loginAccess; 											
	@Autowired 														
	CatalogService catalogService; 											
	@Autowired
	ProductService productService; 								
	@Autowired
	StorageServiceInterface storageService;
	@Autowired
	SharedCatalogService sharedCatalogService;
	
	/**
	 * Display the inventory by id with all products
	 * @param logId
	 * @param model
	 * @return inventory page
	 */
	@GetMapping("/") 																	
	public String showInventory(@RequestParam(name="logid", required=true) int logId, 
								Model model,
								Authentication auth) { 						
		
		String username = auth.getName();										
		int userId = loginAccess.getIdByUsername(username);								
		
		CatalogModel catalog = catalogService.getCatalogById(logId); 							
		
		 List<SharedCatalogModel> sharedUsers =
		            sharedCatalogService.getSharedUsersForCatalog(logId);
		 
		model.addAttribute("title", catalog.getName() + " Catalog");					
		model.addAttribute("color", catalog.getColor());							
		model.addAttribute("logid", logId); 											
		model.addAttribute("products", productService.getAllProductsByCatalog(logId)); 									
		model.addAttribute("userid", userId);
		model.addAttribute("username", username);
		model.addAttribute("isShared", false);
	    model.addAttribute("sharedUsers", sharedUsers);
	    model.addAttribute("canEdit", true);
	    
		return "catalog"; 												
	}
	
	/**
	 * Show the edit catalog page
	 * @param catalogId
	 * @param model
	 * @param auth
	 * @return catalog_form page
	 */
	@GetMapping("/edit/")
	public String showEditCatalog(@RequestParam("logid") int catalogId,
	                              Model model,
	                              Authentication auth) {

	    String username = auth.getName();
	    int userId = loginAccess.getIdByUsername(username);

	    CatalogModel catalog = catalogService.getCatalogById(catalogId);

	    model.addAttribute("title", "Edit Catalog " + catalog.getName());
	    model.addAttribute("catalog", catalog);
	    model.addAttribute("logId", catalogId);
	    model.addAttribute("userId", userId);
	    model.addAttribute("created", "Created: " + Utilities.formatDate(catalog.getCreatedDate()));
	    model.addAttribute("updated", "Last updated: " + Utilities.formatDate(catalog.getUpdatedDate()));
	    model.addAttribute("username", username);

	    model.addAttribute("mode", "edit");

	    return "catalog_form";
	}
	
	/**
	 * Update the catalog with the given catalogId
	 * @param catalog
	 * @param imageFile
	 * @param catalogId
	 * @param auth
	 * @return redirect to catalog page
	 * @throws IOException
	 */
	@PostMapping("/edit/")
	public String updateCatalog(@Valid @ModelAttribute CatalogModel catalog,
            @RequestPart("newimage") MultipartFile imageFile,
            @RequestParam(name="logid") int catalogId,
            Authentication auth) throws IOException {

		catalog.setCatalogId(catalogId);

		// Update catalog in DB
		catalogService.updateCatalog(catalog, imageFile);

		return "redirect:/user/catalog/?logid=" + catalogId;
	}
	
	/**
	 * Delete the catalog with the given catalogId
	 * @param catalogId
	 * @param redirectAttributes
	 * @param auth
	 * @return redirect to inventory page
	 */
	@GetMapping("/delete/{logid}")
    public String showDeleteInventory(@PathVariable("logid") int catalogId,
                                      RedirectAttributes redirectAttributes,
                                      Authentication auth) {

		// show success/failure message based on deletion result
        if (catalogService.deleteCatalog(catalogId)) {
            redirectAttributes.addAttribute("message", "Catalog deleted successfully.");
        } else {
            redirectAttributes.addAttribute("message", "Catalog could not be deleted. Please try again.");
        }

        return "redirect:/user/inventory";
    }
}
