/**
 * RegistrationController.java
 * This class is the controller for the registration page.
 * It handles the GET and POST requests for the registration page.
 */
package com.gcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.gcu.business.RegistrationService;
import com.gcu.model.*;

/**
 * This class is the controller for the registration page.
 * It handles the GET and POST requests for the registration page.
 */
@Controller										
@RequestMapping("/registration")				
public class RegistrationController {	
	
	@Autowired									
	private RegistrationService service;	
	
	/**
	 * Injects the RegistrationService used to handle user registration
	 * @param registrationService RegistrationService to handle user registration
	 */
	public RegistrationController(RegistrationService registrationService) {	
		this.service = registrationService;				
	}
	
	/**
	 * Display the registration form
	 * @param model Model to pass data to the view
	 * @return String view name to render
	 */
	@GetMapping("/")
	public String display(Model model) {
		model.addAttribute("title", "Register");		
		model.addAttribute("registrationModel", new RegistrationModel());				
		return "registration";								
	}
	
	/**
	 * Process the Registration Form
	 * @param registration RegistrationModel populated with form data
	 * @param bindingResult BindingResult to check for validation errors
	 * @param model Model to pass data back to view
	 * @return String view name to render
	 */
	@PostMapping("/doRegister")
	public String doRegister(
	        @Valid @ModelAttribute RegistrationModel registration,
	        BindingResult bindingResult,
	        Model model) {
		
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("title", "Register");
	        model.addAttribute("registrationModel", registration);
	        return "registration";
	    }

	    RegistrationResult result = service.register(registration);

	    if (!result.success()) {
	        model.addAttribute("title", "Register");
	        model.addAttribute("error", result.message());
	        return "registration";
	    }

	    return "redirect:/login/";
	}
}
