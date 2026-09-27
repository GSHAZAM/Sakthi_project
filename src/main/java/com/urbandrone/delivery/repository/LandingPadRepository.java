package com.urbandrone.delivery.repository;

import com.urbandrone.delivery.model.LandingPad;
import com.urbandrone.delivery.model.LandingPadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandingPadRepository extends JpaRepository<LandingPad, Long> {
    List<LandingPad> findByStatus(LandingPadStatus status);
    long countByStatus(LandingPadStatus status);
    boolean existsByPadName(String padName);
}
