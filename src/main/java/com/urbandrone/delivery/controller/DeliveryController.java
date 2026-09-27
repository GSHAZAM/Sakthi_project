package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.*;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final PackageService packageService;
    private final AssignmentService assignmentService;

    @GetMapping
    public String listDeliveries(Model model) {
        model.addAttribute("deliveries", deliveryService.findAllDeliveries());
        model.addAttribute("unassignedPackages", packageService.filterByStatus(PackageStatus.REGISTERED));
        model.addAttribute("deliveryStatuses", DeliveryStatus.values());
        return "admin/deliveries";
    }

    @PostMapping("/assign")
    public String createAutoDelivery(@RequestParam Long packageId, RedirectAttributes redirectAttributes) {
        Package pkg = packageService.findById(packageId).orElse(null);
        if (pkg == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Package not found!");
            return "redirect:/admin/deliveries";
        }
        try {
            Delivery delivery = assignmentService.createAndAssignDelivery(pkg);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Automated Dispatch Successful! Assigned Drone: " + delivery.getDrone().getDroneName() +
                            ", Pad: " + delivery.getLandingPad().getPadName() +
                            ", Locker: " + delivery.getLocker().getLockerNumber());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resource Allocation Failed: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error: " + e.getMessage());
        }
        return "redirect:/admin/deliveries";
    }

    @PostMapping("/status/{id}")
    public String updateDeliveryStatus(@PathVariable Long id,
                                       @RequestParam DeliveryStatus status,
                                       RedirectAttributes redirectAttributes) {
        try {
            deliveryService.advanceDeliveryStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Delivery status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating status: " + e.getMessage());
        }
        return "redirect:/admin/deliveries";
    }
}
