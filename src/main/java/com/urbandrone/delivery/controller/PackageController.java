package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.model.PackageStatus;
import com.urbandrone.delivery.model.User;
import com.urbandrone.delivery.service.PackageService;
import com.urbandrone.delivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;
    private final UserService userService;

    @GetMapping
    public String listPackages(@RequestParam(required = false) String search,
                               @RequestParam(required = false) PackageStatus statusFilter,
                               Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("packages", packageService.searchPackages(search));
            model.addAttribute("search", search);
        } else if (statusFilter != null) {
            model.addAttribute("packages", packageService.filterByStatus(statusFilter));
            model.addAttribute("selectedStatus", statusFilter);
        } else {
            model.addAttribute("packages", packageService.findAllPackages());
        }

        model.addAttribute("customers", userService.findCustomers());
        model.addAttribute("packageStatuses", PackageStatus.values());
        if (!model.containsAttribute("newPackage")) {
            Package p = new Package();
            p.setTrackingNumber(packageService.generateTrackingNumber());
            model.addAttribute("newPackage", p);
        }
        return "admin/packages";
    }

    @PostMapping("/add")
    public String addPackage(@Valid @ModelAttribute("newPackage") Package pkg,
                             BindingResult result,
                             @RequestParam Long customerId,
                             RedirectAttributes redirectAttributes) {
        User customer = userService.findById(customerId).orElse(null);
        if (customer == null) {
            result.rejectValue("customer", "error.package", "Selected customer does not exist");
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newPackage", result);
            redirectAttributes.addFlashAttribute("newPackage", pkg);
            return "redirect:/admin/packages";
        }
        pkg.setCustomer(customer);
        packageService.createPackage(pkg);
        redirectAttributes.addFlashAttribute("successMessage", "Package created successfully with tracking number: " + pkg.getTrackingNumber());
        return "redirect:/admin/packages";
    }

    @PostMapping("/edit/{id}")
    public String editPackage(@PathVariable Long id,
                              @Valid @ModelAttribute Package pkg,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid package data!");
            return "redirect:/admin/packages";
        }
        try {
            packageService.updatePackage(id, pkg);
            redirectAttributes.addFlashAttribute("successMessage", "Package updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/delete/{id}")
    public String deletePackage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            packageService.deletePackage(id);
            redirectAttributes.addFlashAttribute("successMessage", "Package deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting package: " + e.getMessage());
        }
        return "redirect:/admin/packages";
    }
}
