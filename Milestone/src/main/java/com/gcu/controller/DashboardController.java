/**
 * DashboardController.java
 * This is the controller for the dashboard page.
 * It handles the GET request for the dashboard page and returns the dashboard view.
 */
package com.gcu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * This is the controller for the dashboard page.
 * It handles the GET request for the dashboard page and returns the dashboard view.
 */
@Controller
@RequestMapping("/user/dashboard")
public class DashboardController {

	/**
	 * Display the dashboard page
	 * @param model
	 * @return dashboard page
	 */
	@GetMapping("/")
    public String displayDashboard(Model model, Authentication authentication) {
		
		String username = authentication.getName();											
                
		model.addAttribute("title", "My Dashboard");										
        model.addAttribute("message", "Welcome " + username + " to the Dashboard!");		
        model.addAttribute("username", username);			

        return "dashboard"; 															
    }
}