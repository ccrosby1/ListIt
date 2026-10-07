/**
 * AccountController.java
 * This class is the controller for the account page. It handles the requests for the account page and updates the user information.
 */
package com.gcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.UserAccountService;
import com.gcu.model.UserModel;

import jakarta.validation.Valid;

/**
 * This class is the controller for the account page. It handles the requests for the account page and updates the user information.
 */
@Controller
@RequestMapping("/user/account")													
public class AccountController {
	@Autowired
	private UserAccountService accountService;
	 
	/** 
	  * Constructor
	 * @param registrationService
	 */
	public AccountController() {}
	
	/**
	 * Display the account page
	 * @return account page
	 */
	@GetMapping("/")
	public String display(Model model) {
	    model.addAttribute("userAcct", accountService.getCurrentUserAccount());
	    model.addAttribute("title", "Account");
	    return "account_edit";
	}

	/**
	 * Update the user account information
	 * @param user - the user model
	 * @param bindingResult - the binding result
	 * @param redirectAttributes - the redirect attributes
	 * @return account page or dashboard page
	 */
	@PostMapping("/update/")
	public String updateAccount(
	        @Valid @ModelAttribute("userAcct") UserModel user,
	        BindingResult bindingResult,
	        RedirectAttributes redirectAttributes) {

		// validation errors - return to form
	    if (bindingResult.hasErrors()) {
	        return "account_edit";
	    }

	    // attempt update via catalogService
	    if (!accountService.updateCurrentUser(user)) {
	        return "account_edit";
	    }

	    // success - redirect to dashboard
	    redirectAttributes.addFlashAttribute("success", "Account updated successfully");
	    return "redirect:/user/dashboard/";
	}
}
