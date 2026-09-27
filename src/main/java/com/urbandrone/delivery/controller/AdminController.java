package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PackageService packageService;
    private final DroneService droneService;
    private final LandingPadService landingPadService;
    private final LockerService lockerService;
    private final DeliveryService deliveryService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCustomers", userService.countTotalCustomers());
        model.addAttribute("totalPackages", packageService.countTotalPackages());
        model.addAttribute("availableDrones", droneService.countAvailableDrones());
        model.addAttribute("availableLandingPads", landingPadService.countAvailablePads());
        model.addAttribute("availableLockers", lockerService.countAvailableLockers());
        model.addAttribute("activeDeliveries", deliveryService.countActiveDeliveries());
        model.addAttribute("completedDeliveries", deliveryService.countCompletedDeliveries());
        
        model.addAttribute("recentDeliveries", deliveryService.findRecentDeliveries());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }
}
