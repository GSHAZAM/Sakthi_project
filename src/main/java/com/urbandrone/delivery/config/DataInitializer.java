package com.urbandrone.delivery.config;

import com.urbandrone.delivery.model.*;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.repository.*;
import com.urbandrone.delivery.service.AssignmentService;
import com.urbandrone.delivery.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final DroneRepository droneRepository;
    private final LandingPadRepository landingPadRepository;
    private final LockerRepository lockerRepository;
    private final DeliveryRepository deliveryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssignmentService assignmentService;
    private final NotificationService notificationService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // 1. Create Admin
            User admin = User.builder()
                    .name("System Administrator")
                    .email("admin@urbandrone.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+91 9876543210")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);

            // 2. Create Customers
            User customer1 = User.builder()
                    .name("John Doe")
                    .email("customer@gmail.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+91 9123456780")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer1);

            User customer2 = User.builder()
                    .name("Sarah Jenkins")
                    .email("sarah.j@example.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+91 9876501234")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer2);

            User customer3 = User.builder()
                    .name("Dr. Rajesh Kumar")
                    .email("rajesh.k@innovate.org")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+91 9443322110")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer3);

            User customer4 = User.builder()
                    .name("Anita Sharma")
                    .email("anita.s@aerospace.io")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+91 9554433221")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer4);

            // 3. Create 3 Drones
            Drone d1 = droneRepository.save(Drone.builder().droneName("SkyLifter X1").model("AeroPayload 5000").capacity(5.0).batteryLevel(95).status(DroneStatus.AVAILABLE).build());
            Drone d2 = droneRepository.save(Drone.builder().droneName("AeroHawk 500").model("HawkExpress V2").capacity(8.0).batteryLevel(88).status(DroneStatus.IN_FLIGHT).build());
            Drone d3 = droneRepository.save(Drone.builder().droneName("CyberFlyer Z").model("HeavyLift Ultra").capacity(12.0).batteryLevel(92).status(DroneStatus.AVAILABLE).build());

            // 4. Create 4 Landing Pads
            LandingPad pad1 = landingPadRepository.save(LandingPad.builder().padName("Pad Alpha - Coimbatore Metro").location("TIDEL Park, Coimbatore").capacity(4).status(LandingPadStatus.AVAILABLE).build());
            LandingPad pad2 = landingPadRepository.save(LandingPad.builder().padName("Pad Beta - Chennai Tech Hub").location("OMR Tech Corridor, Chennai").capacity(6).status(LandingPadStatus.OCCUPIED).build());
            LandingPad pad3 = landingPadRepository.save(LandingPad.builder().padName("Pad Gamma - Madurai Hub").location("Ring Road Station, Madurai").capacity(3).status(LandingPadStatus.AVAILABLE).build());
            LandingPad pad4 = landingPadRepository.save(LandingPad.builder().padName("Pad Delta - Trichy Complex").location("Central Logistics Park, Trichy").capacity(5).status(LandingPadStatus.MAINTENANCE).build());

            // 5. Create 6 Lockers
            Locker locker1 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-101").location("Pad Alpha - Coimbatore Metro").size("MEDIUM").status(LockerStatus.AVAILABLE).build());
            Locker locker2 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-102").location("Pad Alpha - Coimbatore Metro").size("LARGE").status(LockerStatus.AVAILABLE).build());
            Locker locker3 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-201").location("Pad Beta - Chennai Tech Hub").size("SMALL").status(LockerStatus.RESERVED).build());
            Locker locker4 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-202").location("Pad Beta - Chennai Tech Hub").size("MEDIUM").status(LockerStatus.OCCUPIED).build());
            Locker locker5 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-301").location("Pad Gamma - Madurai Hub").size("LARGE").status(LockerStatus.OCCUPIED).build());
            Locker locker6 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-302").location("Pad Gamma - Madurai Hub").size("MEDIUM").status(LockerStatus.AVAILABLE).build());

            // 6. Create 5 Packages
            Package pkg1 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0001").packageName("Electronic Components").description("Industrial FPGA robotics controller board").weight(2.5).source("Coimbatore").destination("Chennai").customer(customer1).status(PackageStatus.IN_TRANSIT).build());
            Package pkg2 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0002").packageName("Refrigerated Diagnostic Serum").description("Bio-medical lab sample kit").weight(1.5).source("Coimbatore Lab").destination("Madurai Hub").customer(customer1).status(PackageStatus.READY_FOR_DRONE).build());
            Package pkg3 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0003").packageName("Emergency Satellite Transceiver").description("Waterproof avionics unit").weight(4.2).source("Bangalore").destination("Chennai Tech Hub").customer(customer2).status(PackageStatus.ARRIVED_AT_PAD).build());
            Package pkg4 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0004").packageName("Encrypted Document Dossier").description("Sealed security envelope").weight(0.8).source("Coimbatore Chambers").destination("Madurai Station").customer(customer3).status(PackageStatus.STORED_IN_LOCKER).build());
            Package pkg5 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0005").packageName("Precision Laser Sensor").description("Calibrated rangefinder optics").weight(1.2).source("Chennai Lab").destination("Coimbatore Metro").customer(customer4).status(PackageStatus.DELIVERED).build());

            // 7. Seed 3 Deliveries
            Delivery del1 = Delivery.builder()
                    .pkg(pkg1)
                    .customer(customer1)
                    .drone(d2)
                    .landingPad(pad2)
                    .locker(locker4)
                    .status(DeliveryStatus.IN_TRANSIT)
                    .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(20))
                    .build();
            deliveryRepository.save(del1);

            Delivery del2 = Delivery.builder()
                    .pkg(pkg2)
                    .customer(customer1)
                    .drone(d1)
                    .landingPad(pad1)
                    .locker(locker1)
                    .status(DeliveryStatus.DRONE_ASSIGNED)
                    .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(40))
                    .build();
            deliveryRepository.save(del2);

            Delivery del3 = Delivery.builder()
                    .pkg(pkg4)
                    .customer(customer3)
                    .drone(d3)
                    .landingPad(pad3)
                    .locker(locker5)
                    .status(DeliveryStatus.READY_FOR_COLLECTION)
                    .estimatedDeliveryTime(LocalDateTime.now().minusMinutes(15))
                    .build();
            deliveryRepository.save(del3);

            // 8. Create Notifications
            notificationService.createNotification(customer1, "Package UDN-2026-0001 is IN TRANSIT from Coimbatore to Chennai.", "DELIVERY_UPDATE");
            notificationService.createNotification(customer1, "Drone SkyLifter X1 assigned for package UDN-2026-0002.", "DELIVERY_UPDATE");
            notificationService.createNotification(customer3, "Package UDN-2026-0004 stored in LOCKER-301 at Madurai Hub. Ready for pickup!", "PACKAGE_READY");
        }
    }
}
