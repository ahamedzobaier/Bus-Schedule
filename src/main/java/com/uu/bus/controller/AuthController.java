package com.uu.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uu.bus.model.User;
import com.uu.bus.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // --- SIGNUP ---
    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        try {
            userService.registerUser(user);
            // Add ?success=true to the URL
            return "redirect:/login?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    // --- LOGIN ---
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String emailOrId, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.login(emailOrId, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/home";
        }
        model.addAttribute("error", "Invalid ID/Email or Password");
        return "login";
    }

    // --- FORGOT PASSWORD ---
    // 1. Only one GET method to show the page
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    // 2. Only one POST method to handle the logic
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("emailOrId") String emailOrId, Model model) {
        // Here you can later add: userService.sendResetLink(emailOrId);
        model.addAttribute("message", "If an account exists for " + emailOrId + ", a reset link has been sent.");
        return "forgot-password";
    }

    // --- LOGOUT ---
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
