package com.urbandrone.delivery.controller;

import com.urbandrone.delivery.model.LandingPadStatus;
import com.urbandrone.delivery.model.LockerStatus;
import com.urbandrone.delivery.service.LandingPadService;
import com.urbandrone.delivery.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final LandingPadService landingPadService;
    private final LockerService lockerService;

    @GetMapping
    public String viewResources(Model model) {
        model.addAttribute("landingPads", landingPadService.findAllLandingPads());
        model.addAttribute("padStatuses", LandingPadStatus.values());
        model.addAttribute("lockers", lockerService.findAllLockers());
        model.addAttribute("lockerStatuses", LockerStatus.values());
        return "admin/resources";
    }
}
