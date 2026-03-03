package com.uu.bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.uu.bus.model.BusSchedule;
import com.uu.bus.repository.BusScheduleRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ScheduleController {

    @Autowired
    private BusScheduleRepository scheduleRepo;

    @GetMapping("/schedule/{locationName}")
    public String showSchedule(@PathVariable String locationName,
            @RequestParam(defaultValue = "Sunday") String day,
            HttpSession session, Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<BusSchedule> upSchedules = scheduleRepo.findByLocationNameAndDayOfWeekAndDirection(locationName, day, "UP");
        List<BusSchedule> downSchedules = scheduleRepo.findByLocationNameAndDayOfWeekAndDirection(locationName, day, "DOWN");

        model.addAttribute("locationName", locationName);
        model.addAttribute("currentDay", day);
        model.addAttribute("upSchedules", upSchedules);
        model.addAttribute("downSchedules", downSchedules);
        model.addAttribute("days", List.of("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

        return "schedule";
    }
}
