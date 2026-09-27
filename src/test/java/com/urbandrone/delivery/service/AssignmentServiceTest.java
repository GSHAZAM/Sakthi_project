package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.*;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AssignmentServiceTest {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private LandingPadRepository landingPadRepository;

    @Autowired
    private LockerRepository lockerRepository;

    private User testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = User.builder()
                .name("Test User")
                .email("test.user@example.com")
                .password("password123")
                .phone("+1 555-9999")
                .role(UserRole.CUSTOMER)
                .build();
        userRepository.save(testCustomer);
    }

    @Test
    void testAutomaticAssignmentSuccess() {
        Drone drone = droneRepository.save(Drone.builder()
                .droneName("TestDrone-99")
                .model("Model-A")
                .capacity(15.0)
                .batteryLevel(100)
                .status(DroneStatus.AVAILABLE)
                .build());

        LandingPad pad = landingPadRepository.save(LandingPad.builder()
                .padName("TestPad-99")
                .location("Test Location")
                .capacity(2)
                .status(LandingPadStatus.AVAILABLE)
                .build());

        Locker locker = lockerRepository.save(Locker.builder()
                .lockerNumber("TEST-LOCKER-99")
                .location("Test Location")
                .size("MEDIUM")
                .status(LockerStatus.AVAILABLE)
                .build());

        Package pkg = packageRepository.save(Package.builder()
                .trackingNumber("UDN-TEST-0001")
                .packageName("Test Delivery Item")
                .weight(2.0)
                .source("Origin")
                .destination("Destination Pad")
                .customer(testCustomer)
                .status(PackageStatus.REGISTERED)
                .build());

        Delivery delivery = assignmentService.createAndAssignDelivery(pkg);

        assertNotNull(delivery);
        assertNotNull(delivery.getId());
        assertEquals(DeliveryStatus.DRONE_ASSIGNED, delivery.getStatus());
        assertNotNull(delivery.getDrone());
        assertNotNull(delivery.getLandingPad());
        assertNotNull(delivery.getLocker());

        // Verify status updates on assigned entities
        assertEquals(DroneStatus.ASSIGNED, delivery.getDrone().getStatus());
        assertEquals(LandingPadStatus.OCCUPIED, delivery.getLandingPad().getStatus());
        assertEquals(LockerStatus.RESERVED, delivery.getLocker().getStatus());
    }
}
