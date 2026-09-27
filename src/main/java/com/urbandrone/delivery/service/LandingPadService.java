package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.LandingPad;
import com.urbandrone.delivery.model.LandingPadStatus;
import com.urbandrone.delivery.repository.LandingPadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LandingPadService {

    private final LandingPadRepository landingPadRepository;

    public LandingPad createLandingPad(LandingPad pad) {
        if (landingPadRepository.existsByPadName(pad.getPadName())) {
            throw new IllegalArgumentException("Landing Pad name '" + pad.getPadName() + "' already exists");
        }
        if (pad.getStatus() == null) {
            pad.setStatus(LandingPadStatus.AVAILABLE);
        }
        return landingPadRepository.save(pad);
    }

    public List<LandingPad> findAllLandingPads() {
        return landingPadRepository.findAll();
    }

    public Optional<LandingPad> findById(Long id) {
        return landingPadRepository.findById(id);
    }

    public List<LandingPad> findAvailablePads() {
        return landingPadRepository.findByStatus(LandingPadStatus.AVAILABLE);
    }

    public LandingPad updateLandingPad(Long id, LandingPad updated) {
        LandingPad pad = landingPadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Landing Pad not found with id: " + id));
        pad.setPadName(updated.getPadName());
        pad.setLocation(updated.getLocation());
        pad.setCapacity(updated.getCapacity());
        pad.setStatus(updated.getStatus());
        return landingPadRepository.save(pad);
    }

    public LandingPad updateLandingPadStatus(Long id, LandingPadStatus status) {
        LandingPad pad = landingPadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Landing Pad not found with id: " + id));
        pad.setStatus(status);
        return landingPadRepository.save(pad);
    }

    public void deleteLandingPad(Long id) {
        landingPadRepository.deleteById(id);
    }

    public long countAvailablePads() {
        return landingPadRepository.countByStatus(LandingPadStatus.AVAILABLE);
    }
}
