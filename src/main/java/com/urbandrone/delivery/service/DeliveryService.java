package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.*;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PackageRepository packageRepository;
    private final DroneRepository droneRepository;
    private final LandingPadRepository landingPadRepository;
    private final LockerRepository lockerRepository;
    private final NotificationService notificationService;

    public List<Delivery> findAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> findRecentDeliveries() {
        return deliveryRepository.findTop10ByOrderByIdDesc();
    }

    public List<Delivery> findDeliveriesByCustomer(User customer) {
        return deliveryRepository.findByCustomerOrderByIdDesc(customer);
    }

    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    public Optional<Delivery> findByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber.trim());
    }

    public long countActiveDeliveries() {
        return deliveryRepository.countActiveDeliveries();
    }

    public long countCompletedDeliveries() {
        return deliveryRepository.countByStatus(DeliveryStatus.COMPLETED);
    }

    public long countActiveDeliveriesByCustomer(User customer) {
        return deliveryRepository.countActiveDeliveriesByCustomer(customer);
    }

    public long countCompletedDeliveriesByCustomer(User customer) {
        return deliveryRepository.countCompletedDeliveriesByCustomer(customer);
    }

    @Transactional
    public Delivery advanceDeliveryStatus(Long deliveryId, DeliveryStatus targetStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with id: " + deliveryId));

        delivery.setStatus(targetStatus);
        Package pkg = delivery.getPkg();
        Drone drone = delivery.getDrone();
        LandingPad pad = delivery.getLandingPad();
        Locker locker = delivery.getLocker();

        switch (targetStatus) {
            case IN_TRANSIT:
                pkg.setStatus(PackageStatus.IN_TRANSIT);
                if (drone != null) {
                    drone.setStatus(DroneStatus.IN_FLIGHT);
                    droneRepository.save(drone);
                }
                break;
            case ARRIVED:
                pkg.setStatus(PackageStatus.ARRIVED_AT_PAD);
                if (pad != null) {
                    pad.setStatus(LandingPadStatus.OCCUPIED);
                    landingPadRepository.save(pad);
                }
                break;
            case LOCKER_ASSIGNED:
            case READY_FOR_COLLECTION:
                pkg.setStatus(PackageStatus.STORED_IN_LOCKER);
                if (locker != null) {
                    locker.setStatus(LockerStatus.OCCUPIED);
                    lockerRepository.save(locker);
                }
                // Drone is released after pad landing & locker placement
                if (drone != null) {
                    drone.setStatus(DroneStatus.AVAILABLE);
                    drone.setBatteryLevel(Math.max(10, drone.getBatteryLevel() - 15));
                    droneRepository.save(drone);
                }
                if (pad != null) {
                    pad.setStatus(LandingPadStatus.AVAILABLE);
                    landingPadRepository.save(pad);
                }
                notificationService.createNotification(delivery.getCustomer(),
                        "Package " + pkg.getTrackingNumber() + " is safely stored in Locker " +
                                (locker != null ? locker.getLockerNumber() : "") + " and ready for collection!",
                        "PACKAGE_READY");
                break;
            case COMPLETED:
                pkg.setStatus(PackageStatus.DELIVERED);
                delivery.setCompletedAt(LocalDateTime.now());
                if (locker != null) {
                    locker.setStatus(LockerStatus.AVAILABLE);
                    locker.setCurrentPackage(null);
                    lockerRepository.save(locker);
                }
                if (drone != null && drone.getStatus() == DroneStatus.IN_FLIGHT) {
                    drone.setStatus(DroneStatus.AVAILABLE);
                    droneRepository.save(drone);
                }
                if (pad != null && pad.getStatus() == LandingPadStatus.OCCUPIED) {
                    pad.setStatus(LandingPadStatus.AVAILABLE);
                    landingPadRepository.save(pad);
                }
                notificationService.createNotification(delivery.getCustomer(),
                        "Package " + pkg.getTrackingNumber() + " has been successfully collected. Thank you for using Urban Drone Delivery!",
                        "DELIVERY_COMPLETED");
                break;
            default:
                break;
        }

        packageRepository.save(pkg);
        return deliveryRepository.save(delivery);
    }
}
