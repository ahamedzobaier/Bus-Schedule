package com.uu.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uu.bus.model.BusSchedule;
import com.uu.bus.model.Location;
import com.uu.bus.repository.AdminRepository;
import com.uu.bus.repository.BusScheduleRepository;
import com.uu.bus.repository.LocationRepository;
import com.uu.bus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private BusScheduleRepository scheduleRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private LocationRepository locationRepo;

    // Helper method to check session
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("admin") != null;
    }

    // --- Admin Login ---
    @GetMapping("/login")
    public String adminLogin() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        return adminRepo.findByUsername(username)
                .filter(a -> a.getPassword().equals(password))
                .map(a -> {
                    session.setAttribute("admin", a);
                    return "redirect:/admin/dashboard";
                }).orElse("redirect:/admin/login?error");
    }

    // --- Admin Dashboard ---
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("schedules", scheduleRepo.findAll());
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        return "admin-dashboard";
    }

    // --- User Management ---
    @GetMapping("/users")
    public String manageUsers(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("users", userRepo.findAll());
        return "admin-users";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long id, @RequestParam String name,
            @RequestParam String universityId, @RequestParam String department,
            RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        userRepo.findById(id).ifPresent(user -> {
            user.setName(name);
            user.setUniversityId(universityId);
            user.setDepartment(department);
            userRepo.save(user);
        });
        ra.addFlashAttribute("message", "User updated successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}") // <--- Use GetMapping instead
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        if (id != null) {
            try {
                userRepo.deleteById(id);
                ra.addFlashAttribute("message", "User deleted successfully!");
            } catch (Exception e) {
                // This triggers if the user is linked to a bus schedule
                ra.addFlashAttribute("message", "Cannot delete: User is linked to other data.");
            }
        }
        return "redirect:/admin/users";
    }

    // --- Location Management ---
    @GetMapping("/location")
    public String manageLocations(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("locations", locationRepo.findAll());
        return "admin-locations";
    }

    @PostMapping("/add-location")
    public String addLocation(@RequestParam String name, RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        Location loc = new Location();
        loc.setName(name);
        locationRepo.save(loc);
        ra.addFlashAttribute("success", "Location added successfully!");
        return "redirect:/admin/location";
    }

    @PostMapping("/location/update")
    public String updateLocation(@RequestParam Long id, @RequestParam String name,
            RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        locationRepo.findById(id).ifPresent(loc -> {
            loc.setName(name);
            locationRepo.save(loc);
        });
        ra.addFlashAttribute("message", "Location updated!");
        return "redirect:/admin/location";
    }

    @PostMapping("/location/delete/{id}")
    public String deleteLocation(@PathVariable Long id, RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        if (id != null) {
            locationRepo.deleteById(id);
            ra.addFlashAttribute("message", "Location deleted!");
        }
        return "redirect:/admin/location";
    }

    // --- Schedule Management ---
    @PostMapping("/add-schedule")
    public String addSchedule(@ModelAttribute BusSchedule schedule, @RequestParam Long locationId,
            RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        locationRepo.findById(locationId).ifPresent(loc -> {
            schedule.setLocation(loc);
            scheduleRepo.save(schedule);
        });
        ra.addFlashAttribute("success", "Schedule added!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/delete-schedule/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        if (id != null) {
            scheduleRepo.deleteById(id);
            ra.addFlashAttribute("message", "Schedule deleted!");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/schedule/update")
    public String updateSchedule(@ModelAttribute BusSchedule schedule, @RequestParam Long locationId,
            RedirectAttributes ra, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        locationRepo.findById(locationId).ifPresent(loc -> {
            schedule.setLocation(loc);
            scheduleRepo.save(schedule);
        });
        ra.addFlashAttribute("message", "Schedule updated!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
