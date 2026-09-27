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
                    .name("Chief Operations Admin")
                    .email("admin@urbandrone.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+1 800-555-0199")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);

            // 2. Create Customers
            User customer1 = User.builder()
                    .name("John Doe")
                    .email("customer@gmail.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 555-014-8899")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer1);

            User customer2 = User.builder()
                    .name("Sarah Jenkins")
                    .email("sarah.j@example.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 555-019-2234")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer2);

            User customer3 = User.builder()
                    .name("Dr. Robert Chen")
                    .email("robert.chen@innovate.org")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 555-018-9900")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer3);

            User customer4 = User.builder()
                    .name("Elena Vance")
                    .email("elena.v@aerospace.io")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 555-017-3344")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer4);

            // 3. Create Drones Fleet
            Drone d1 = droneRepository.save(Drone.builder().droneName("SkyLifter X1").model("AeroPayload 5000").capacity(5.0).batteryLevel(95).status(DroneStatus.AVAILABLE).build());
            Drone d2 = droneRepository.save(Drone.builder().droneName("AeroHawk 500").model("HawkExpress V2").capacity(8.0).batteryLevel(88).status(DroneStatus.IN_FLIGHT).build());
            Drone d3 = droneRepository.save(Drone.builder().droneName("CyberFlyer Z").model("HeavyLift Ultra").capacity(12.0).batteryLevel(92).status(DroneStatus.AVAILABLE).build());
            Drone d4 = droneRepository.save(Drone.builder().droneName("Falcon-Heavy X").model("TitanFlight V3").capacity(15.0).batteryLevel(100).status(DroneStatus.AVAILABLE).build());
            Drone d5 = droneRepository.save(Drone.builder().droneName("Nimbus-Drone 9").model("SkyRunner Pro").capacity(4.0).batteryLevel(74).status(DroneStatus.IN_FLIGHT).build());
            Drone d6 = droneRepository.save(Drone.builder().droneName("VoltWing V2").model("EcoFlight 200").capacity(6.0).batteryLevel(15).status(DroneStatus.MAINTENANCE).build());
            Drone d7 = droneRepository.save(Drone.builder().droneName("AeroPulse 3000").model("PulseLift Max").capacity(10.0).batteryLevel(82).status(DroneStatus.AVAILABLE).build());
            Drone d8 = droneRepository.save(Drone.builder().droneName("SkySwift Alpha").model("SwiftDeliver V1").capacity(3.5).batteryLevel(60).status(DroneStatus.AVAILABLE).build());

            // 4. Create Landing Pads
            LandingPad pad1 = landingPadRepository.save(LandingPad.builder().padName("Pad Alpha - Downtown Metro").location("742 Evergreen Terrace, Sector 4").capacity(4).status(LandingPadStatus.AVAILABLE).build());
            LandingPad pad2 = landingPadRepository.save(LandingPad.builder().padName("Pad Beta - Tech Hub").location("100 Innovation Way, Building B").capacity(6).status(LandingPadStatus.OCCUPIED).build());
            LandingPad pad3 = landingPadRepository.save(LandingPad.builder().padName("Pad Gamma - Riverfront").location("55 Riverfront Drive, Pier 9").capacity(3).status(LandingPadStatus.AVAILABLE).build());
            LandingPad pad4 = landingPadRepository.save(LandingPad.builder().padName("Pad Delta - Medical Complex").location("12 Hospital Plaza, North Wing").capacity(5).status(LandingPadStatus.OCCUPIED).build());
            LandingPad pad5 = landingPadRepository.save(LandingPad.builder().padName("Pad Epsilon - Airport Logistics").location("89 Terminal Ave, Hangar 4").capacity(8).status(LandingPadStatus.AVAILABLE).build());
            LandingPad pad6 = landingPadRepository.save(LandingPad.builder().padName("Pad Zeta - West Port Deck").location("33 Ocean Boulevard, Dock 12").capacity(2).status(LandingPadStatus.MAINTENANCE).build());

            // 5. Create Package Lockers
            Locker locker1 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-101").location("Pad Alpha - Downtown Metro").size("MEDIUM").status(LockerStatus.AVAILABLE).build());
            Locker locker2 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-102").location("Pad Alpha - Downtown Metro").size("LARGE").status(LockerStatus.AVAILABLE).build());
            Locker locker3 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-201").location("Pad Beta - Tech Hub").size("SMALL").status(LockerStatus.RESERVED).build());
            Locker locker4 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-202").location("Pad Beta - Tech Hub").size("MEDIUM").status(LockerStatus.OCCUPIED).build());
            Locker locker5 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-301").location("Pad Gamma - Riverfront").size("LARGE").status(LockerStatus.OCCUPIED).build());
            Locker locker6 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-302").location("Pad Gamma - Riverfront").size("MEDIUM").status(LockerStatus.AVAILABLE).build());
            Locker locker7 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-401").location("Pad Delta - Medical Complex").size("SMALL").status(LockerStatus.OCCUPIED).build());
            Locker locker8 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-402").location("Pad Delta - Medical Complex").size("MEDIUM").status(LockerStatus.RESERVED).build());
            Locker locker9 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-501").location("Pad Epsilon - Airport Logistics").size("LARGE").status(LockerStatus.AVAILABLE).build());
            Locker locker10 = lockerRepository.save(Locker.builder().lockerNumber("LOCKER-601").location("Pad Zeta - West Port Deck").size("SMALL").status(LockerStatus.AVAILABLE).build());

            // 6. Create Packages
            Package pkg1 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0001").packageName("Refrigerated Bio-Diagnostic Kit").description("Urgent temperature-controlled blood serum samples").weight(1.5).source("Central Bio Lab, Metro West").destination("Pad Alpha - Downtown Metro").customer(customer1).status(PackageStatus.READY_FOR_DRONE).build());
            Package pkg2 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0002").packageName("High-Frequency Microcontroller Suite").description("Industrial FPGA robotics controller board").weight(2.2).source("TechDepot Warehouse, Bay 3").destination("Pad Beta - Tech Hub").customer(customer1).status(PackageStatus.IN_TRANSIT).build());
            Package pkg3 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0003").packageName("Emergency Satellite Transceiver Unit").description("Waterproof avionics communication module").weight(4.5).source("Avionics Center, Runway 4").destination("Pad Delta - Medical Complex").customer(customer2).status(PackageStatus.ARRIVED_AT_PAD).build());
            Package pkg4 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0004").packageName("Encrypted Legal Document Dossier").description("Tamper-evident legal certificate envelope").weight(0.8).source("Downtown Legal Chambers").destination("Pad Gamma - Riverfront").customer(customer3).status(PackageStatus.STORED_IN_LOCKER).build());
            Package pkg5 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0005").packageName("Precision Optical Rangefinder Sensor").description("LiDAR calibration laser module").weight(1.2).source("Photonics Lab Annex").destination("Pad Alpha - Downtown Metro").customer(customer4).status(PackageStatus.DELIVERED).build());
            Package pkg6 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0006").packageName("Cryo-Preserved Enzyme Vials").description("Medical research reagent package").weight(2.0).source("Biotech Institute, Sector 2").destination("Pad Delta - Medical Complex").customer(customer3).status(PackageStatus.REGISTERED).build());
            Package pkg7 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0007").packageName("Titanium Drone Motor Component").description("High-torque brushless motor set").weight(3.8).source("Advanced Propulsion Lab").destination("Pad Epsilon - Airport Logistics").customer(customer4).status(PackageStatus.REGISTERED).build());
            Package pkg8 = packageRepository.save(Package.builder().trackingNumber("UDN-2026-0008").packageName("High-Output Solar Cell Array").description("Experimental photovoltaic panel kit").weight(5.1).source("Renewables Depot").destination("Pad Beta - Tech Hub").customer(customer2).status(PackageStatus.REGISTERED).build());

            // 7. Seed Active Deliveries with Detailed Statuses
            Delivery del1 = Delivery.builder()
                    .pkg(pkg1)
                    .customer(customer1)
                    .drone(d1)
                    .landingPad(pad1)
                    .locker(locker1)
                    .status(DeliveryStatus.DRONE_ASSIGNED)
                    .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(25))
                    .build();
            deliveryRepository.save(del1);

            Delivery del2 = Delivery.builder()
                    .pkg(pkg2)
                    .customer(customer1)
                    .drone(d2)
                    .landingPad(pad2)
                    .locker(locker4)
                    .status(DeliveryStatus.IN_TRANSIT)
                    .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(12))
                    .build();
            deliveryRepository.save(del2);

            Delivery del3 = Delivery.builder()
                    .pkg(pkg3)
                    .customer(customer2)
                    .drone(d5)
                    .landingPad(pad4)
                    .locker(locker7)
                    .status(DeliveryStatus.ARRIVED)
                    .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(5))
                    .build();
            deliveryRepository.save(del3);

            Delivery del4 = Delivery.builder()
                    .pkg(pkg4)
                    .customer(customer3)
                    .drone(d3)
                    .landingPad(pad3)
                    .locker(locker5)
                    .status(DeliveryStatus.READY_FOR_COLLECTION)
                    .estimatedDeliveryTime(LocalDateTime.now().minusMinutes(10))
                    .build();
            deliveryRepository.save(del4);

            Delivery del5 = Delivery.builder()
                    .pkg(pkg5)
                    .customer(customer4)
                    .drone(d7)
                    .landingPad(pad1)
                    .locker(locker2)
                    .status(DeliveryStatus.COMPLETED)
                    .completedAt(LocalDateTime.now().minusHours(2))
                    .estimatedDeliveryTime(LocalDateTime.now().minusHours(2).plusMinutes(35))
                    .build();
            deliveryRepository.save(del5);

            // 8. Create Notifications
            notificationService.createNotification(customer1, "Drone SkyLifter X1 assigned to deliver package UDN-2026-0001.", "DELIVERY_UPDATE");
            notificationService.createNotification(customer1, "Package UDN-2026-0002 is currently IN FLIGHT via AeroHawk 500.", "DELIVERY_UPDATE");
            notificationService.createNotification(customer2, "Drone Nimbus-Drone 9 has landed at Pad Delta - Medical Complex.", "PAD_UPDATE");
            notificationService.createNotification(customer3, "Package UDN-2026-0004 is stored in LOCKER-301. Ready for collection!", "PACKAGE_READY");
            notificationService.createNotification(customer4, "Package UDN-2026-0005 has been collected. Thank you for using UrbanDrone!", "DELIVERY_COMPLETED");
        }
    }
}
