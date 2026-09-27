package com.urbandrone.delivery.repository;

import com.urbandrone.delivery.model.Delivery;
import com.urbandrone.delivery.model.DeliveryStatus;
import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByCustomerOrderByIdDesc(User customer);
    List<Delivery> findByStatus(DeliveryStatus status);
    Optional<Delivery> findByPkg(Package pkg);
    
    @Query("SELECT d FROM Delivery d WHERE d.pkg.trackingNumber = :trackingNumber")
    Optional<Delivery> findByTrackingNumber(@Param("trackingNumber") String trackingNumber);

    long countByStatus(DeliveryStatus status);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status != 'COMPLETED'")
    long countActiveDeliveries();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.customer = :customer AND d.status != 'COMPLETED'")
    long countActiveDeliveriesByCustomer(@Param("customer") User customer);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.customer = :customer AND d.status = 'COMPLETED'")
    long countCompletedDeliveriesByCustomer(@Param("customer") User customer);

    List<Delivery> findTop10ByOrderByIdDesc();
}
