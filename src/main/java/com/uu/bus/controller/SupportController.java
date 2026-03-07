package com.uu.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uu.bus.model.Feedback;
import com.uu.bus.model.SupportContact;
import com.uu.bus.repository.FeedbackRepository;
import com.uu.bus.repository.SupportContactRepository; // Added this import

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SupportContactRepository contactRepo; // Added this repository

    @GetMapping
    public String showSupportPage(HttpSession session, Model model) {
        // 1. Security Check
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // 2. Fetch Contact Info (ID 1)
        SupportContact contact = contactRepo.findById(1L)
                .orElse(new SupportContact(1L, "017XXXXXXXX", "transport@uu.edu.bd", "Main Campus", "8 AM - 5 PM"));

        model.addAttribute("contact", contact);

        // 3. Prepare empty feedback object for the form
        model.addAttribute("feedback", new Feedback());

        return "support";
    }

    @PostMapping("/submit-feedback")
    public String saveFeedback(@ModelAttribute Feedback feedback, RedirectAttributes ra) {
        try {
            feedbackRepository.save(feedback);
            ra.addFlashAttribute("success", "Your feedback has been submitted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Something went wrong. Please try again.");
        }
        return "redirect:/support";
    }
}
