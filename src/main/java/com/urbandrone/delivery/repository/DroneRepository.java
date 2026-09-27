package com.urbandrone.delivery.repository;

import com.urbandrone.delivery.model.Drone;
import com.urbandrone.delivery.model.DroneStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findByStatus(DroneStatus status);
    
    @Query("SELECT d FROM Drone d WHERE d.status = 'AVAILABLE' AND d.batteryLevel >= :minBattery AND d.capacity >= :minCapacity ORDER BY d.batteryLevel DESC")
    List<Drone> findAvailableDronesForPayload(@Param("minBattery") int minBattery, @Param("minCapacity") double minCapacity);

    long countByStatus(DroneStatus status);
    boolean existsByDroneName(String droneName);
}
