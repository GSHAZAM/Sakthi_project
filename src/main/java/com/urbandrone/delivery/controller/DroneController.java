package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.Drone;
import com.urbandrone.delivery.model.DroneStatus;
import com.urbandrone.delivery.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public String listDrones(Model model) {
        model.addAttribute("drones", droneService.findAllDrones());
        model.addAttribute("droneStatuses", DroneStatus.values());
        if (!model.containsAttribute("drone")) {
            model.addAttribute("drone", new Drone());
        }
        return "admin/drones";
    }

    @PostMapping("/add")
    public String addDrone(@Valid @ModelAttribute("drone") Drone drone,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.drone", result);
            redirectAttributes.addFlashAttribute("drone", drone);
            return "redirect:/admin/drones";
        }
        try {
            droneService.createDrone(drone);
            redirectAttributes.addFlashAttribute("successMessage", "Drone added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/drones";
    }

    @PostMapping("/edit/{id}")
    public String editDrone(@PathVariable Long id,
                            @Valid @ModelAttribute Drone drone,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid drone form input!");
            return "redirect:/admin/drones";
        }
        try {
            droneService.updateDrone(id, drone);
            redirectAttributes.addFlashAttribute("successMessage", "Drone updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/drones";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam DroneStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            droneService.updateDroneStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Drone status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/drones";
    }

    @PostMapping("/delete/{id}")
    public String deleteDrone(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            droneService.deleteDrone(id);
            redirectAttributes.addFlashAttribute("successMessage", "Drone deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete drone: " + e.getMessage());
        }
        return "redirect:/admin/drones";
    }
}
