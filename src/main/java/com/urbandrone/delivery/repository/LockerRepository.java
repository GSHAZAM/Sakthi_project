package com.urbandrone.delivery.repository;

import com.urbandrone.delivery.model.Locker;
import com.urbandrone.delivery.model.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockerRepository extends JpaRepository<Locker, Long> {
    List<Locker> findByStatus(LockerStatus status);
    long countByStatus(LockerStatus status);
    boolean existsByLockerNumber(String lockerNumber);
}
