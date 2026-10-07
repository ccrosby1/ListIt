/**
 * CategoryController.java
 * This class is the controller for the Category model.
 */
package com.gcu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.gcu.business.CategoryService;
import com.gcu.data.LoginAccess;
import com.gcu.model.CategoryModel;

/**
 * This is the contoller for the Category Page.
 * It handles all the requests for the Category page.
 */
@Controller
@RequestMapping("/user/categories")
public class CategoryController {
	
	@Autowired
	LoginAccess loginAccess; 											
	@Autowired
	private CategoryService categoryService;	
	
	/**
	 * Display the categories page with all categories for the authenticated user.
	 * @param model The model to pass data to the view.
	 * @param authentication The authentication object containing user details.
	 * @return The view name for the categories page.
	 */
	@GetMapping("/")
	public String showCategories(Model model,
								 Authentication authentication) {
		String username = authentication.getName();										
		int userId = loginAccess.getIdByUsername(username);								
		
		List<CategoryModel> categories = categoryService.getAllCategories(userId);
	    Map<Integer, String> categoryNames = categoryService.getStringCategories(userId);

	    model.addAttribute("categories", categories);
	    model.addAttribute("categoryNames", categoryNames);
	    model.addAttribute("username", username);
	    model.addAttribute("userID", userId);
	    model.addAttribute("title", "Categories");

	    return "categories";
	}
	
	/**
	 * Displays the form to add a new category.
	 * @param model The model to pass data to the view.
	 * @param auth The authentication object containing user details.
	 * @return The view name for the category form.
	 */
	@GetMapping("/add")
    public String showAddCategoryForm(Model model, Authentication auth) {
        String username = auth.getName();
        int userId = loginAccess.getIdByUsername(username);

        model.addAttribute("category", new CategoryModel());
        model.addAttribute("parents", categoryService.getAllParentCategories(userId));
        model.addAttribute("title", "Add Category");
        model.addAttribute("mode", "add");

        return "category-form";
    }
	
	/**
	 * Handles the submission of the add category form.
	 * @param category The category model containing the new category details.
	 * @param auth The authentication object containing user details.
	 * @return A redirect to the categories page after successful addition.
	 */
	@PostMapping("/add")
    public String handleAddCategory(
            @ModelAttribute CategoryModel category,
            Authentication auth) {

        String username = auth.getName();
        category.setUserId(loginAccess.getIdByUsername(username));
        // assign category to the authenticated user
        categoryService.addCategory(category);

        return "redirect:/user/categories/";
    }
	
	/**
	 * Displays the form to edit an existing category.
	 * @param id The ID of the category to edit.
	 * @param model The model to pass data to the view.
	 * @param auth The authentication object containing user details.
	 * @return The view name for the category form populated with existing category details.
	 */
	@GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable int id,
                                       Model model,
                                       Authentication auth) {

        String username = auth.getName();
        int userId = loginAccess.getIdByUsername(username);

        CategoryModel category = categoryService.getCategoryById(id, userId);
        // load category to populate edit form
        model.addAttribute("category", category);
        model.addAttribute("parents", categoryService.getAllParentCategories(userId));
        model.addAttribute("title", "Edit Category");
        model.addAttribute("mode", "edit");

        return "category-form";
    }
	
	/**
	 * Handles the submission of the edit category form.
	 * @param id The ID of the category being edited.
	 * @param category The category model containing the updated category details.
	 * @return A redirect to the categories page after successful update.
	 */
	@PostMapping("/edit/{id}")
    public String handleEditCategory(@PathVariable int id,
                                     @ModelAttribute CategoryModel category) {

        categoryService.updateCategory(id, category.getName());
        categoryService.updateCategoryParent(id, category.getParentId());

        return "redirect:/user/categories/";
    }
	
	/**
	 * Handles the deletion of a category
	 * @param id The ID of the category to delete
	 * @param auth The authentication object containing user deatils
	 * @return A redirect to the categories page after successful deletion
	 */
	@GetMapping("/delete/{id}")
	    public String deleteCategory(@PathVariable int id, Authentication auth) {
	        String username = auth.getName();
	        int userId = loginAccess.getIdByUsername(username);

	        categoryService.deleteCategory(id, userId);

	        return "redirect:/user/categories/";
	    }
}
