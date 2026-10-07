/**
 * InventoryController.java
 * Controller for the inventory page
 * Handles the requests for the inventory page
 */
package com.gcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.InventoryServiceInterface;
import com.gcu.business.SharedCatalogService;
import com.gcu.data.LoginAccess;
import com.gcu.data.entity.CatalogEntity;
import com.gcu.model.CatalogModel;

import jakarta.validation.Valid;

/**
 * InventoryController class
 * Handles the requests for the inventory page
 */
@Controller																		
@RequestMapping("/user/inventory") 													
public class InventoryController {

	@Autowired
	private LoginAccess loginAccess;
	@Autowired
	private InventoryServiceInterface inventoryService;
	@Autowired
	private SharedCatalogService sharedCatalogService;
	
	/**
	 * Handles the GET request for the inventory page
	 * @param model Model object
	 * @param user UserDetails object for the authenticated user
	 * @return String view name
	 */
	@GetMapping({"", "/"})
	public String showInventory(Model model, @AuthenticationPrincipal UserDetails user) {
		int userId = loginAccess.getIdByUsername(user.getUsername());
		
		model.addAttribute("title", "Inventory");
        model.addAttribute("userid", userId);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("imagelocation", "/images/uploads");
        
        model.addAttribute("invs", inventoryService.getInventory(userId));
        model.addAttribute("sharedInvs", sharedCatalogService.getSharedCatalogs(userId));
        
        return "inventory";
	}
	
	/**
	 * Handles the GET request for adding a new catalog
	 * @param model Model object
	 * @param user UserDetails object for the authenticated user
	 * @return String view name
	 */
	@GetMapping("/add/")
	public String showAddCatalog(Model model,
	                             @AuthenticationPrincipal UserDetails user) {

	    int userId = loginAccess.getIdByUsername(user.getUsername());

	    model.addAttribute("title", "Add Catalog");
	    model.addAttribute("catalog", new CatalogModel());
	    model.addAttribute("userId", userId);
	    model.addAttribute("username", user.getUsername());
	    model.addAttribute("mode", "add");

	    return "catalog_form";
	}
	
	/**
	 * Handles the POST request for adding a new catalog
	 * @param catalog CatalogModel object
	 * @param result BindingResult object
	 * @param model Model object
	 * @param imageFile MultipartFile object for the image
	 * @param user UserDetails object for the authenticated user
	 * @param redirectAttributes RedirectAttributes object for flash attributes
	 * @return String view name
	 */
	@PostMapping("/add/")
	public String addCatalog(@Valid @ModelAttribute CatalogModel catalog, 
			BindingResult result, 
			Model model, 
			@RequestPart("newimage") MultipartFile imageFile, 
			@AuthenticationPrincipal UserDetails user, 
			RedirectAttributes redirectAttributes) {
		
		int userId = loginAccess.getIdByUsername(user.getUsername());
		
		// re-display form when validation fails
		if (result.hasErrors()) {
		    model.addAttribute("title", "Add Catalog");
		    model.addAttribute("mode", "add");
		    model.addAttribute("userId", userId);
		    model.addAttribute("username", user.getUsername());
		    return "catalog_form";
		}
		
		try {
			CatalogEntity createdCatalog = inventoryService.addCatalogWithImage(catalog, imageFile, userId);
			
			redirectAttributes.addFlashAttribute("success", "Catalog added successfully: " + createdCatalog.getName());
			return "redirect:/user/inventory";
			
		} catch (Exception e) {
			model.addAttribute("title", "Add Catalog");
	        model.addAttribute("mode", "add");
	        model.addAttribute("error", e.getMessage());
	        model.addAttribute("userId", userId);
	        model.addAttribute("username", user.getUsername());
	        model.addAttribute("catalog", catalog);
	        return "catalog_form";
		}
	}	
}