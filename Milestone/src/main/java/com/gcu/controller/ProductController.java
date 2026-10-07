/**
 * ProductController.java
 * This is the controller for the Product Model
 * It handles the requests for adding, updating, deleting, and viewing products.
 **/
package com.gcu.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.CategoryService;
import com.gcu.business.ProductService;
import com.gcu.business.StorageServiceInterface;
import com.gcu.utilities.Utilities;
import com.gcu.data.LoginAccess;
import com.gcu.data.entity.ProductEntity;
import com.gcu.model.CategoryModel;
import com.gcu.model.ProductModel;

import jakarta.validation.Valid;

/**
 * ProductController class
 * Handles the requests for the product page
 */
@Controller
@RequestMapping("/user/product")
public class ProductController {
	@Autowired																	
	private ProductService productService;										
	@Autowired
	StorageServiceInterface storageService;										
	@Autowired
	LoginAccess loginAccess; 											
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * Display the product list page
	 * @param model
	 * @param catalogId
	 * @return product list page
	 */
	@GetMapping("/") 																	
	public String showProductList(Model model, 											
					@RequestParam(name="logid", required=true) int catalogId,			
					Authentication auth) { 									

		String username = auth.getName();										
		
		model.addAttribute("username", username);								
		model.addAttribute("title", "Product List"); 							
		model.addAttribute("id", catalogId); 									
				
		return "product"; 														
	}
	
	/**
	 * Display the add product page
	 * @param model
	 * @param catalogId
	 * @return add product page
	 */
	@GetMapping("/add/") 																			
	public String showAddProductForm(Model model, 													
									 @RequestParam(name="logid", required=true) int catalogId,		
									 Authentication authentication) {								

		String username = authentication.getName();										
		int userId = loginAccess.getIdByUsername(username);	
		
		// load all categories for the current user
	    List<CategoryModel> allCategories = categoryService.getAllCategories(userId);

	    model.addAttribute("categories", allCategories);
	    model.addAttribute("username", username);
	    model.addAttribute("title", "Add Product");
	    model.addAttribute("catalogId", catalogId);
	    model.addAttribute("productModel", new ProductModel());
	    model.addAttribute("logid", catalogId);

	    return "product_form";										
	}
	
	/**
	 * Process the add product form
	 * @param product
	 * @param bindingResult
	 * @param catalogId
	 * @param redirectAttributes
	 * @param model
	 * @return redirect to product list page
	 */
	@PostMapping("/add/")
	public String addProduct(@Valid @ModelAttribute("productModel") ProductModel product,
	                         BindingResult bindingResult,
	                         @RequestPart("newimage") MultipartFile imageFile,
	                         @RequestParam(name = "logid", required = true) int catalogId,
	                         RedirectAttributes redirectAttributes,
	                         Model model,
	                         Authentication authentication) {

		String username = authentication.getName();
	    int userId = loginAccess.getIdByUsername(username);
	    List<CategoryModel> allCategories = categoryService.getAllCategories(userId);

	    // re-display form if validation fails
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("categories", allCategories);
	        model.addAttribute("title", "Add Product");
	        model.addAttribute("catalogId", catalogId);
	        model.addAttribute("logid", catalogId);
	        return "product_form";
	    }

	    product.setCatalogId(catalogId);

	    ProductEntity created = productService.createProductAndHandleImage(product, imageFile);

	    // re-display form if creation fails
	    if (created != null) {
	    	redirectAttributes.addAttribute("logid", catalogId);
	    	return "redirect:/user/catalog/";
	    }

	    model.addAttribute("categories", allCategories);
	    model.addAttribute("username", username);
	    model.addAttribute("title", "Add Product");
	    model.addAttribute("catalogId", catalogId);
	    model.addAttribute("logid", catalogId);
	    model.addAttribute("error", "Product could not be added. Please try again.");

	    return "product_form";
	}
	
	/**
	 * Process the delete product request
	 * @param productId
	 * @param catalogId
	 * @param redirectAttributes
	 * @param authentication
	 * @return redirect to product list page
	 */
	@GetMapping("/delete/{productId}")															
	public String deleteProduct(@PathVariable int productId, 						
								@RequestParam(name="logid", required=true) int catalogId,		
								RedirectAttributes redirectAttributes,							
								Authentication authentication) {								
		
		// show success/failure message after delete attempt
		if(productService.deleteProduct(productId)) {					
			redirectAttributes.addFlashAttribute("message", 
					"Product deleted successfully."); 								
			redirectAttributes.addFlashAttribute("messageType", "success"); 		
		} else {
			redirectAttributes.addFlashAttribute("message", 
					"Product could not be deleted. Please try again."); 			
			redirectAttributes.addFlashAttribute("messageType", "failure"); 		
		}
		
		redirectAttributes.addAttribute("logid", catalogId);
		return "redirect:/user/catalog/";						
	}
	
	/**
	 * Display the edit product page
	 * @param productId
	 * @param catalogId
	 * @param model
	 * @return edit product page
	 */
	@GetMapping("/edit/{productId}")																
	public String showEditProductForm(@PathVariable int productId, 					
									  @RequestParam(name="logid", required=true) int catalogId,		
									  Model model,													
									  Authentication authentication) {								
		
		String username = authentication.getName();										
		int userId = loginAccess.getIdByUsername(username);								
		
		ProductModel product = productService.getProductById(productId);				
		
		// ensure product exists before rendering edit form
		if (product == null) {															
			model.addAttribute("error", "Product not found.");							
			model.addAttribute("logid", catalogId);
			return "redirect:/user/catalog/";						
		}
													
		List<CategoryModel> allCategories = categoryService.getAllCategories(userId);
		model.addAttribute("categories", allCategories);						
		model.addAttribute("username", username);								
		model.addAttribute("title", "Edit Product");							
		model.addAttribute("productModel", product);							
		model.addAttribute("catalogId", catalogId);								
		model.addAttribute("logid", catalogId);									
		model.addAttribute("updated", "Last updated: " + 
						   Utilities.formatDate(product.getUpdatedDate()));		
		model.addAttribute("created", "Created: " + 
						   Utilities.formatDate(product.getCreatedDate()));		

		return "product_form";													
	}
	
	/**
	 * Process the update product form
	 * @param productId
	 * @param catalogId
	 * @param imageFile
	 * @param product
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param authentication
	 * @return
	 */
	@PostMapping("/edit/{productId}")															
	public String updateProduct(@Valid @ModelAttribute("productModel") ProductModel product, 	
								BindingResult bindingResult,									
								@PathVariable int productId, 					
								@RequestParam(name="logid", required=true) int catalogId,		
								@RequestPart("newimage") MultipartFile imageFile, 				
								Model model,													
								RedirectAttributes redirectAttributes,							
								Authentication authentication) {
		
		String username = authentication.getName();
	    int userId = loginAccess.getIdByUsername(username);
	    List<CategoryModel> allCategories = categoryService.getAllCategories(userId);

	    // re-display form if validation fails
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("categories", allCategories);
	        model.addAttribute("username", username);
	        model.addAttribute("title", "Edit Product");
	        model.addAttribute("catalogId", catalogId);
	        model.addAttribute("logid", catalogId);
	        return "product_form";
	    }

	    product.setCatalogId(catalogId);
	    product.setId(productId);

	    // update product image if a new one was uploaded
	    storageService.updateProductImage(product, imageFile);

	    if (productService.updateProduct(product)) {
	    	redirectAttributes.addAttribute("logid", catalogId);
	    	return "redirect:/user/catalog/";
	    }

	    model.addAttribute("categories", allCategories);
	    model.addAttribute("username", username);
	    model.addAttribute("title", "Edit Product");
	    model.addAttribute("catalogId", catalogId);
	    model.addAttribute("logid", catalogId);
	    model.addAttribute("error", "Product could not be updated. Please try again.");

	    return "product_form";											
	}
	
	/**
	 * Get all categories for the authenticated user
	 * @param user
	 * @return list of category names
	 */
	@GetMapping("/user/categories/all")
	@ResponseBody
	public List<String> getAllCategories(@AuthenticationPrincipal UserDetails user) {
	    int userId = loginAccess.getIdByUsername(user.getUsername());
	    return categoryService.getStringCategories(userId).values().stream().toList();
	}
}