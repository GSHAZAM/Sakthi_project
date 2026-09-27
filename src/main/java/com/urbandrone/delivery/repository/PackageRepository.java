package com.urbandrone.delivery.repository;

import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.model.PackageStatus;
import com.urbandrone.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    Optional<Package> findByTrackingNumber(String trackingNumber);
    List<User> findByCustomer(User customer); // Optional helper
    List<Package> findByCustomerOrderByIdDesc(User customer);
    List<Package> findByStatus(PackageStatus status);
    
    @Query("SELECT p FROM Package p WHERE p.trackingNumber LIKE %:query% OR LOWER(p.packageName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.destination) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Package> searchPackages(@Param("query") String query);

    @Query("SELECT p FROM Package p WHERE p.customer = :customer AND (p.trackingNumber LIKE %:query% OR LOWER(p.packageName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Package> searchCustomerPackages(@Param("customer") User customer, @Param("query") String query);

    long countByStatus(PackageStatus status);
}
