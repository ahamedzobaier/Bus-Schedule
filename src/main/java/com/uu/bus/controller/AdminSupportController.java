package com.uu.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uu.bus.model.SupportContact;
import com.uu.bus.repository.FeedbackRepository;
import com.uu.bus.repository.SupportContactRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/support")
public class AdminSupportController {

    @Autowired
    private SupportContactRepository contactRepo;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping
    public String showAdminSupportPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        SupportContact contact = contactRepo.findById(1L)
                .orElse(new SupportContact(1L, "017XXXXXXXX", "transport@uu.edu.bd", "Main Campus", "8 AM - 5 PM"));
        model.addAttribute("contact", contact);
        model.addAttribute("allFeedback", feedbackRepository.findAll());

        // Change this line:
        return "support-management";
    }

    @PostMapping("/delete-feedback")
    public String deleteFeedback(@RequestParam("id") Long id, RedirectAttributes ra) {
        try {
            feedbackRepository.deleteById(id);
            ra.addFlashAttribute("success", "Feedback deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to delete feedback.");
        }
        return "redirect:/admin/support";
    }

    @PostMapping("/update-contact")
    public String updateContact(@ModelAttribute SupportContact contact, RedirectAttributes ra) {
        // Force ID to 1 to ensure we are always overwriting the same office record
        contact.setId(1L);
        contactRepo.save(contact);
        ra.addFlashAttribute("success", "Transport Office details updated!");
        return "redirect:/admin/support";
    }
}
