package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.Locker;
import com.urbandrone.delivery.model.LockerStatus;
import com.urbandrone.delivery.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LockerService {

    private final LockerRepository lockerRepository;

    public Locker createLocker(Locker locker) {
        if (lockerRepository.existsByLockerNumber(locker.getLockerNumber())) {
            throw new IllegalArgumentException("Locker number '" + locker.getLockerNumber() + "' already exists");
        }
        if (locker.getStatus() == null) {
            locker.setStatus(LockerStatus.AVAILABLE);
        }
        return lockerRepository.save(locker);
    }

    public List<Locker> findAllLockers() {
        return lockerRepository.findAll();
    }

    public Optional<Locker> findById(Long id) {
        return lockerRepository.findById(id);
    }

    public List<Locker> findAvailableLockers() {
        return lockerRepository.findByStatus(LockerStatus.AVAILABLE);
    }

    public Locker updateLocker(Long id, Locker updated) {
        Locker locker = lockerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found with id: " + id));
        locker.setLockerNumber(updated.getLockerNumber());
        locker.setLocation(updated.getLocation());
        locker.setSize(updated.getSize());
        locker.setStatus(updated.getStatus());
        return lockerRepository.save(locker);
    }

    public Locker updateLockerStatus(Long id, LockerStatus status) {
        Locker locker = lockerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found with id: " + id));
        locker.setStatus(status);
        return lockerRepository.save(locker);
    }

    public void deleteLocker(Long id) {
        lockerRepository.deleteById(id);
    }

    public long countAvailableLockers() {
        return lockerRepository.countByStatus(LockerStatus.AVAILABLE);
    }
}
