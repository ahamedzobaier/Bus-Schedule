package com.uu.bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uu.bus.model.Location;
import com.uu.bus.repository.LocationRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private LocationRepository locationRepo;

    // This handles both the root URL and the /home URL
    @GetMapping({"/", "/home"})
    public String showHome(HttpSession session, Model model) {

        // 1. Security Check: Redirect to login if no user is in session
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        // 2. Fetch REAL locations from the database (not a hardcoded list)
        List<Location> allLocations = locationRepo.findAll();

        // 3. Add to model
        model.addAttribute("locations", allLocations);

        // 4. Return the home view
        return "home";
    }

    @GetMapping("/notifications")
    public String showNotifications(HttpSession session, Model model) {
        // Optional: Check if user is logged in
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // This returns "notifications.html" from your templates folder
        return "notifications";
    }

    @GetMapping("/support")
    public String showSupport(HttpSession session) {
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        return "support";
    }
}
