/**
 * HomeController.java
 * This class is the controller for the home page
 * Currently it only redirects to the login page
 */
package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	/**
	 * Redirect to the login page
	 * @return
	 */
	@GetMapping("/")
	public String home() {
		return "redirect:/login/";
	}
}
