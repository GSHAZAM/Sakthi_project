package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.LandingPad;
import com.urbandrone.delivery.model.LandingPadStatus;
import com.urbandrone.delivery.service.LandingPadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/landing-pads")
@RequiredArgsConstructor
public class LandingPadController {

    private final LandingPadService landingPadService;

    @GetMapping
    public String listLandingPads(Model model) {
        model.addAttribute("landingPads", landingPadService.findAllLandingPads());
        model.addAttribute("padStatuses", LandingPadStatus.values());
        if (!model.containsAttribute("landingPad")) {
            model.addAttribute("landingPad", new LandingPad());
        }
        return "admin/landing-pads";
    }

    @PostMapping("/add")
    public String addLandingPad(@Valid @ModelAttribute("landingPad") LandingPad pad,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.landingPad", result);
            redirectAttributes.addFlashAttribute("landingPad", pad);
            return "redirect:/admin/landing-pads";
        }
        try {
            landingPadService.createLandingPad(pad);
            redirectAttributes.addFlashAttribute("successMessage", "Landing pad added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/landing-pads";
    }

    @PostMapping("/edit/{id}")
    public String editLandingPad(@PathVariable Long id,
                                 @Valid @ModelAttribute LandingPad pad,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid landing pad data!");
            return "redirect:/admin/landing-pads";
        }
        try {
            landingPadService.updateLandingPad(id, pad);
            redirectAttributes.addFlashAttribute("successMessage", "Landing pad updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/landing-pads";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam LandingPadStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            landingPadService.updateLandingPadStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Landing pad status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/landing-pads";
    }

    @PostMapping("/delete/{id}")
    public String deleteLandingPad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            landingPadService.deleteLandingPad(id);
            redirectAttributes.addFlashAttribute("successMessage", "Landing pad deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete landing pad: " + e.getMessage());
        }
        return "redirect:/admin/landing-pads";
    }
}
