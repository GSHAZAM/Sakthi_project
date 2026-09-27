package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.User;
import com.urbandrone.delivery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;
    private final PackageService packageService;
    private final DeliveryService deliveryService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = getAuthenticatedUser(userDetails);
        
        model.addAttribute("customer", customer);
        model.addAttribute("packages", packageService.findByCustomer(customer));
        model.addAttribute("totalPackages", packageService.findByCustomer(customer).size());
        model.addAttribute("activeDeliveries", deliveryService.countActiveDeliveriesByCustomer(customer));
        model.addAttribute("completedDeliveries", deliveryService.countCompletedDeliveriesByCustomer(customer));

        return "customer/dashboard";
    }

    @GetMapping("/packages")
    public String viewPackages(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = getAuthenticatedUser(userDetails);
        model.addAttribute("packages", packageService.findByCustomer(customer));
        return "customer/packages";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = getAuthenticatedUser(userDetails);
        model.addAttribute("user", customer);
        return "customer/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam(required = false) String newPassword,
                                RedirectAttributes redirectAttributes) {
        User customer = getAuthenticatedUser(userDetails);
        try {
            userService.updateUserProfile(customer.getId(), name, phone, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    private User getAuthenticatedUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }
}
