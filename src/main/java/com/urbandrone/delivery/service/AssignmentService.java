package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.*;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final DroneRepository droneRepository;
    private final LandingPadRepository landingPadRepository;
    private final LockerRepository lockerRepository;
    private final DeliveryRepository deliveryRepository;
    private final PackageRepository packageRepository;
    private final NotificationService notificationService;

    @Transactional
    public Delivery createAndAssignDelivery(Package pkg) {
        if (deliveryRepository.findByPkg(pkg).isPresent()) {
            throw new IllegalStateException("A delivery dispatch already exists for package: " + pkg.getTrackingNumber());
        }

        // 1. Find available drone
        List<Drone> availableDrones = droneRepository.findAvailableDronesForPayload(20, pkg.getWeight());
        if (availableDrones.isEmpty()) {
            throw new IllegalStateException("No available drone with sufficient battery and weight capacity (" 
                    + pkg.getWeight() + " kg) for delivery!");
        }
        Drone assignedDrone = availableDrones.get(0);

        // 2. Find available landing pad
        List<LandingPad> availablePads = landingPadRepository.findByStatus(LandingPadStatus.AVAILABLE);
        if (availablePads.isEmpty()) {
            throw new IllegalStateException("No available landing pad for drone dispatch at destination!");
        }
        LandingPad assignedPad = availablePads.get(0);

        // 3. Find available locker
        List<Locker> availableLockers = lockerRepository.findByStatus(LockerStatus.AVAILABLE);
        if (availableLockers.isEmpty()) {
            throw new IllegalStateException("No available package locker at destination site!");
        }
        Locker assignedLocker = availableLockers.get(0);

        // 4. Update resource statuses
        assignedDrone.setStatus(DroneStatus.ASSIGNED);
        droneRepository.save(assignedDrone);

        assignedPad.setStatus(LandingPadStatus.OCCUPIED);
        landingPadRepository.save(assignedPad);

        assignedLocker.setStatus(LockerStatus.RESERVED);
        assignedLocker.setCurrentPackage(pkg);
        lockerRepository.save(assignedLocker);

        // Update package status
        pkg.setStatus(PackageStatus.READY_FOR_DRONE);
        packageRepository.save(pkg);

        // 5. Create delivery
        Delivery delivery = Delivery.builder()
                .pkg(pkg)
                .customer(pkg.getCustomer())
                .drone(assignedDrone)
                .landingPad(assignedPad)
                .locker(assignedLocker)
                .status(DeliveryStatus.DRONE_ASSIGNED)
                .estimatedDeliveryTime(LocalDateTime.now().plusHours(1))
                .build();

        Delivery savedDelivery = deliveryRepository.save(delivery);

        // 6. Create notification
        String message = String.format("Delivery scheduled for package %s! Drone %s assigned to Pad %s and Locker %s.",
                pkg.getTrackingNumber(), assignedDrone.getDroneName(), assignedPad.getPadName(), assignedLocker.getLockerNumber());
        notificationService.createNotification(pkg.getCustomer(), message, "DELIVERY_ASSIGNED");

        return savedDelivery;
    }
}
