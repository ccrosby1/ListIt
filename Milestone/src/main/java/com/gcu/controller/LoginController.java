/**
 * LoginController.java
 * This class is the controller for the login page.
 * It handles the GET and POST requests for the login page.
 * It uses the LoginModel class to bind the form data.
 * It uses the @Controller and @RequestMapping annotations to define the controller and the request mapping.
 * 
 */
package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.gcu.model.LoginModel;

/**
 * This class is the controller for the login page.
 * It handles the GET and POST requests for the login page.
 * It uses the LoginModel class to bind the form data.
 * It uses the @Controller and @RequestMapping annotations to define the controller and the request mapping.

 */
@Controller										
@RequestMapping("/login")						
public class LoginController {
	
	/**
	 * Display the login form
	 * @param model 
	 * @return login page
	 */
	@GetMapping("/")										
	public String display(@RequestParam(required = false) String error,
            Model model) {					
		model.addAttribute("title", "Login");
	    model.addAttribute("loginModel", new LoginModel());

	    if (error != null) {
	        model.addAttribute("error", "Incorrect username or password.");
	    }

	    return "login";									
	}
	
}
