package com.uu.bus.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uu.bus.model.User;
import com.uu.bus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        // 1. Retrieve the user object from the session
        User loggedInUser = (User) session.getAttribute("user");

        // 2. Safety Check: If no user is in session, send them to login
        if (loggedInUser == null || loggedInUser.getId() == null) {
            return "redirect:/login";
        }

        // 3. Database Check: Fetch the latest data using the ID
        // We use Optional to handle the "type safety" warning properly
        Optional<User> userOptional = userRepo.findById(loggedInUser.getId());

        if (userOptional.isPresent()) {
            // If found in DB, put the fresh data in the model
            model.addAttribute("user", userOptional.get());
        } else {
            // If for some reason not in DB, use the session data
            model.addAttribute("user", loggedInUser);
        }

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("name") String name,
            @RequestParam("universityId") String universityId,
            @RequestParam("department") String department,
            HttpSession session,
            Model model) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 1. Fetch the user from the database
        User user = userRepo.findById(loggedInUser.getId()).orElse(null);

        if (user != null) {
            // 2. Update the fields
            user.setName(name);
            user.setUniversityId(universityId);
            user.setDepartment(department);

            // 3. Save to database
            userRepo.save(user);

            // 4. Update the session so the new name appears in the navbar immediately
            session.setAttribute("user", user);
        }

        return "redirect:/profile?updated";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword,
            HttpSession session,
            Model model) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 1. Fetch user from DB
        User user = userRepo.findById(loggedInUser.getId()).orElse(null);

        if (user != null) {
            // 2. Set the new password
            user.setPassword(newPassword);

            // 3. Save back to database
            userRepo.save(user);

            // 4. Update the session so it stays in sync
            session.setAttribute("user", user);

            model.addAttribute("message", "Password updated successfully!");
        }

        return "redirect:/profile?success";
    }
}
