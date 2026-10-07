/**
 * AdminUserController.java
 */
package com.gcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.AdminUserService;
import com.gcu.model.UserModel;

import jakarta.validation.Valid;

/**
 * This class handles the requests for the admin user management page.
 * It allows the admin to view, edit, and delete users.
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

	@Autowired
	private AdminUserService userService;
	
	/**
	 * Constructor
	 * @param userService
	 */
	@GetMapping("/")
	public String listUsers(Model model) {
	    model.addAttribute("users", userService.findAllUsers());
	    model.addAttribute("adminInfo", userService.getAdminDisplayInfo());
	    model.addAttribute("title", "User List");
	    return "userlist";
	}

	/**
	 * Displays the edit user form for the specified user ID.
	 * @param userId The ID of the user to edit.
	 * @param model The model to pass data to the view.
	 * @param redirectAttributes Attributes for flash messages.
	 * @return The view name for editing a user or redirect if user not found.
	 */
	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable("id") int userId, Model model, RedirectAttributes redirectAttributes) {
		UserModel user = userService.getUserForEdit(userId);

		// user not found, redirect with error
	    if (user == null) {
	        redirectAttributes.addFlashAttribute("error", "Error retrieving user");
	        return "redirect:/admin/users/";
	    }

	    model.addAttribute("userinfo", user);
	    model.addAttribute("adminInfo", userService.getAdminDisplayInfo());
	    model.addAttribute("title", "Edit User");
	    return "admin_user_edit";
	}
	
	/**
	 * Updates the user information for the specified user ID.
	 * @param userId The ID of the user to update.
	 * @param user The updated user information.
	 * @param bindingResult The result of validation checks.
	 * @param model The model to pass data to the view.
	 * @param redirectAttributes Attributes for flash messages.
	 * @return Redirects to the user list on success or returns to the edit form on validation failure.
	 */
	@PostMapping("/edit/{id}")
	public String updateUser(@PathVariable("id") int userId,
							@Valid @ModelAttribute("userinfo") UserModel user,
							BindingResult bindingResult,
							Model model,
							RedirectAttributes redirectAttributes) {
		
		// If validation fails, return to the form
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("title", "Edit User");
	        return "admin_user_edit";
	    }

	    boolean updated = userService.updateUser(userId, user);

	    if (updated) {
	        redirectAttributes.addFlashAttribute("message", "User updated successfully");
	        redirectAttributes.addFlashAttribute("messageType", "success");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "Failed to update user");
	        redirectAttributes.addFlashAttribute("messageType", "error");
	    }

	    return "redirect:/admin/users/";
	}

	/**
	 * Deletes the user with the specified user ID.
	 * @param userId The ID of the user to delete.
	 * @param redirectAttributes Attributes for flash messages.
	 * @return Redirects to the user list after deletion attempt.
	 */
	@GetMapping("/delete/")
	public String deleteUser(@RequestParam("user_id") int userId, RedirectAttributes redirectAttributes) {

	    boolean deleted = userService.deleteUserByAdmin(userId);

	    // show success/failure message after delete attempt
	    if (deleted) {
	        redirectAttributes.addFlashAttribute("success", "User deleted successfully");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "Error deleting user");
	    }

	    return "redirect:/admin/users/";
	}
}