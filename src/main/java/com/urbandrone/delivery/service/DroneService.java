package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.Drone;
import com.urbandrone.delivery.model.DroneStatus;
import com.urbandrone.delivery.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;

    public Drone createDrone(Drone drone) {
        if (droneRepository.existsByDroneName(drone.getDroneName())) {
            throw new IllegalArgumentException("Drone name '" + drone.getDroneName() + "' already exists");
        }
        if (drone.getStatus() == null) {
            drone.setStatus(DroneStatus.AVAILABLE);
        }
        return droneRepository.save(drone);
    }

    public List<Drone> findAllDrones() {
        return droneRepository.findAll();
    }

    public Optional<Drone> findById(Long id) {
        return droneRepository.findById(id);
    }

    public List<Drone> findAvailableDrones() {
        return droneRepository.findByStatus(DroneStatus.AVAILABLE);
    }

    public Drone updateDrone(Long id, Drone updated) {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found with id: " + id));
        
        drone.setDroneName(updated.getDroneName());
        drone.setModel(updated.getModel());
        drone.setCapacity(updated.getCapacity());
        drone.setBatteryLevel(updated.getBatteryLevel());
        drone.setStatus(updated.getStatus());
        return droneRepository.save(drone);
    }

    public Drone updateDroneStatus(Long id, DroneStatus status) {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found with id: " + id));
        drone.setStatus(status);
        return droneRepository.save(drone);
    }

    public void deleteDrone(Long id) {
        droneRepository.deleteById(id);
    }

    public long countAvailableDrones() {
        return droneRepository.countByStatus(DroneStatus.AVAILABLE);
    }
}
