package com.urbandrone.delivery.config;

import com.urbandrone.delivery.model.*;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.repository.*;
import com.urbandrone.delivery.service.AssignmentService;
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

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // 1. Create Admin
            User admin = User.builder()
                    .name("System Administrator")
                    .email("admin@urbandrone.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+1 800-555-0199")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);

            // 2. Create Customer
            User customer = User.builder()
                    .name("John Doe")
                    .email("customer@gmail.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 555-014-8899")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer);

            User customer2 = User.builder()
                    .name("Sarah Jenkins")
                    .email("sarah.j@example.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 555-019-2234")
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(customer2);

            // 3. Create 3 Drones
            Drone drone1 = Drone.builder()
                    .droneName("SkyLifter X1")
                    .model("AeroPayload 5000")
                    .capacity(5.0)
                    .batteryLevel(95)
                    .status(DroneStatus.AVAILABLE)
                    .build();
            droneRepository.save(drone1);

            Drone drone2 = Drone.builder()
                    .droneName("AeroHawk 500")
                    .model("HawkExpress V2")
                    .capacity(8.0)
                    .batteryLevel(88)
                    .status(DroneStatus.AVAILABLE)
                    .build();
            droneRepository.save(drone2);

            Drone drone3 = Drone.builder()
                    .droneName("CyberFlyer Z")
                    .model("HeavyLift Ultra")
                    .capacity(10.0)
                    .batteryLevel(90)
                    .status(DroneStatus.AVAILABLE)
                    .build();
            droneRepository.save(drone3);

            // 4. Create 4 Landing Pads
            LandingPad pad1 = LandingPad.builder()
                    .padName("Pad Alpha - Downtown")
                    .location("742 Evergreen Terrace, Sector 4")
                    .capacity(2)
                    .status(LandingPadStatus.AVAILABLE)
                    .build();
            landingPadRepository.save(pad1);

            LandingPad pad2 = LandingPad.builder()
                    .padName("Pad Beta - North Hub")
                    .location("100 Innovation Way, Tech Park")
                    .capacity(4)
                    .status(LandingPadStatus.AVAILABLE)
                    .build();
            landingPadRepository.save(pad2);

            LandingPad pad3 = LandingPad.builder()
                    .padName("Pad Gamma - East Station")
                    .location("55 Riverfront Drive, District 9")
                    .capacity(3)
                    .status(LandingPadStatus.AVAILABLE)
                    .build();
            landingPadRepository.save(pad3);

            LandingPad pad4 = LandingPad.builder()
                    .padName("Pad Delta - West Port")
                    .location("89 Harbour Boulevard, Pier 12")
                    .capacity(2)
                    .status(LandingPadStatus.MAINTENANCE)
                    .build();
            landingPadRepository.save(pad4);

            // 5. Create 6 Lockers
            Locker locker1 = Locker.builder()
                    .lockerNumber("LOCKER-101")
                    .location("Pad Alpha - Downtown")
                    .size("MEDIUM")
                    .status(LockerStatus.AVAILABLE)
                    .build();
            lockerRepository.save(locker1);

            Locker locker2 = Locker.builder()
                    .lockerNumber("LOCKER-102")
                    .location("Pad Alpha - Downtown")
                    .size("LARGE")
                    .status(LockerStatus.AVAILABLE)
                    .build();
            lockerRepository.save(locker2);

            Locker locker3 = Locker.builder()
                    .lockerNumber("LOCKER-201")
                    .location("Pad Beta - North Hub")
                    .size("SMALL")
                    .status(LockerStatus.AVAILABLE)
                    .build();
            lockerRepository.save(locker3);

            Locker locker4 = Locker.builder()
                    .lockerNumber("LOCKER-202")
                    .location("Pad Beta - North Hub")
                    .size("MEDIUM")
                    .status(LockerStatus.AVAILABLE)
                    .build();
            lockerRepository.save(locker4);

            Locker locker5 = Locker.builder()
                    .lockerNumber("LOCKER-301")
                    .location("Pad Gamma - East Station")
                    .size("LARGE")
                    .status(LockerStatus.AVAILABLE)
                    .build();
            lockerRepository.save(locker5);

            Locker locker6 = Locker.builder()
                    .lockerNumber("LOCKER-302")
                    .location("Pad Gamma - East Station")
                    .size("MEDIUM")
                    .status(LockerStatus.AVAILABLE)
                    .build();
            lockerRepository.save(locker6);

            // 6. Create 5 Packages
            Package pkg1 = Package.builder()
                    .trackingNumber("UDN-2026-0001")
                    .packageName("Medical Diagnostic Sample Kit")
                    .description("Urgent refrigerated bio-medical payload")
                    .weight(1.5)
                    .source("Central Bio Lab, Metro West")
                    .destination("742 Evergreen Terrace, Sector 4")
                    .customer(customer)
                    .status(PackageStatus.REGISTERED)
                    .build();
            packageRepository.save(pkg1);

            Package pkg2 = Package.builder()
                    .trackingNumber("UDN-2026-0002")
                    .packageName("High-End Microcontroller Suite")
                    .description("Fragile electronic components box")
                    .weight(2.2)
                    .source("TechDepot Warehouse, Bay 3")
                    .destination("100 Innovation Way, Tech Park")
                    .customer(customer)
                    .status(PackageStatus.REGISTERED)
                    .build();
            packageRepository.save(pkg2);

            Package pkg3 = Package.builder()
                    .trackingNumber("UDN-2026-0003")
                    .packageName("Emergency Satellite Transmitter")
                    .description("Communication array unit")
                    .weight(3.0)
                    .source("Avionics Center, Runway 4")
                    .destination("55 Riverfront Drive, District 9")
                    .customer(customer)
                    .status(PackageStatus.REGISTERED)
                    .build();
            packageRepository.save(pkg3);

            Package pkg4 = Package.builder()
                    .trackingNumber("UDN-2026-0004")
                    .packageName("Legal Document Dossier")
                    .description("Sealed security envelope")
                    .weight(0.8)
                    .source("Downtown Legal Chambers")
                    .destination("742 Evergreen Terrace, Sector 4")
                    .customer(customer2)
                    .status(PackageStatus.REGISTERED)
                    .build();
            packageRepository.save(pkg4);

            Package pkg5 = Package.builder()
                    .trackingNumber("UDN-2026-0005")
                    .packageName("Optics Precision Sensor")
                    .description("Calibrated laser rangefinder")
                    .weight(1.2)
                    .source("Photonics Lab Annex")
                    .destination("100 Innovation Way, Tech Park")
                    .customer(customer2)
                    .status(PackageStatus.REGISTERED)
                    .build();
            packageRepository.save(pkg5);

            // 7. Create 3 Deliveries
            try {
                // Delivery 1: Assigned and in transit
                assignmentService.createAndAssignDelivery(pkg1);
                
                // Delivery 2: Assigned and ready for collection (stored in locker)
                Delivery d2 = assignmentService.createAndAssignDelivery(pkg2);
                d2.setStatus(DeliveryStatus.READY_FOR_COLLECTION);
                d2.getPkg().setStatus(PackageStatus.STORED_IN_LOCKER);
                d2.getLocker().setStatus(LockerStatus.OCCUPIED);
                d2.getDrone().setStatus(DroneStatus.AVAILABLE);
                d2.getLandingPad().setStatus(LandingPadStatus.AVAILABLE);
                deliveryRepository.save(d2);

                // Delivery 3: Completed
                Delivery d3 = assignmentService.createAndAssignDelivery(pkg3);
                d3.setStatus(DeliveryStatus.COMPLETED);
                d3.getPkg().setStatus(PackageStatus.DELIVERED);
                d3.setCompletedAt(LocalDateTime.now().minusHours(1));
                d3.getLocker().setStatus(LockerStatus.AVAILABLE);
                d3.getLocker().setCurrentPackage(null);
                d3.getDrone().setStatus(DroneStatus.AVAILABLE);
                d3.getLandingPad().setStatus(LandingPadStatus.AVAILABLE);
                deliveryRepository.save(d3);

            } catch (Exception e) {
                System.out.println("Initializer note on delivery auto-assignment: " + e.getMessage());
            }
        }
    }
}
