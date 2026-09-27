package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.Delivery;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.service.DeliveryService;
import com.urbandrone.delivery.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TrackingController {

    private final PackageService packageService;
    private final DeliveryService deliveryService;

    @GetMapping("/tracking")
    public String trackPackage(@RequestParam(required = false) String trackingNumber, Model model) {
        if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
            String trimmedNumber = trackingNumber.trim();
            model.addAttribute("searchedTrackingNumber", trimmedNumber);

            Optional<Package> pkgOpt = packageService.findByTrackingNumber(trimmedNumber);
            if (pkgOpt.isPresent()) {
                Package pkg = pkgOpt.get();
                model.addAttribute("package", pkg);
                
                Optional<Delivery> deliveryOpt = deliveryService.findByTrackingNumber(trimmedNumber);
                deliveryOpt.ifPresent(delivery -> model.addAttribute("delivery", delivery));
            } else {
                model.addAttribute("errorMessage", "No package found with tracking number: " + trimmedNumber);
            }
        }
        return "customer/tracking";
    }
}
