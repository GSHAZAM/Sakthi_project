package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.Locker;
import com.urbandrone.delivery.model.LockerStatus;
import com.urbandrone.delivery.service.LockerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/lockers")
@RequiredArgsConstructor
public class LockerController {

    private final LockerService lockerService;

    @GetMapping
    public String listLockers(Model model) {
        model.addAttribute("lockers", lockerService.findAllLockers());
        model.addAttribute("lockerStatuses", LockerStatus.values());
        if (!model.containsAttribute("locker")) {
            model.addAttribute("locker", new Locker());
        }
        return "admin/lockers";
    }

    @PostMapping("/add")
    public String addLocker(@Valid @ModelAttribute("locker") Locker locker,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.locker", result);
            redirectAttributes.addFlashAttribute("locker", locker);
            return "redirect:/admin/lockers";
        }
        try {
            lockerService.createLocker(locker);
            redirectAttributes.addFlashAttribute("successMessage", "Locker added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/lockers";
    }

    @PostMapping("/edit/{id}")
    public String editLocker(@PathVariable Long id,
                             @Valid @ModelAttribute Locker locker,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid locker data!");
            return "redirect:/admin/lockers";
        }
        try {
            lockerService.updateLocker(id, locker);
            redirectAttributes.addFlashAttribute("successMessage", "Locker updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/lockers";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam LockerStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            lockerService.updateLockerStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Locker status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/lockers";
    }

    @PostMapping("/delete/{id}")
    public String deleteLocker(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            lockerService.deleteLocker(id);
            redirectAttributes.addFlashAttribute("successMessage", "Locker deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete locker: " + e.getMessage());
        }
        return "redirect:/admin/lockers";
    }
}
