/**
 * AdminController.java
 */
package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.business.UserAccountService;

/**
 * This class handles requests for the admin dashboard.
 * It displays the admin dashboard and handles requests to view the dashboard by username.
 */
@Controller
@RequestMapping("/admin/dashboard")													
public class AdminController {

	private final UserAccountService accountService;

    public AdminController(UserAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Displays the admin dashboard.
     * Username is retrieved through the catalogService layer for consistency.
     */
    @GetMapping("/")
    public String display(Model model) {

        String username = accountService.getAuthenticatedUsername();

        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");
        model.addAttribute("username", username);

        return "admindashboard";
    }
}