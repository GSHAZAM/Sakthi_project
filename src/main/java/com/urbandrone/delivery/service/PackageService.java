package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.Package;
import com.urbandrone.delivery.model.PackageStatus;
import com.urbandrone.delivery.model.User;
import com.urbandrone.delivery.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    public Package createPackage(Package pkg) {
        if (pkg.getTrackingNumber() == null || pkg.getTrackingNumber().trim().isEmpty()) {
            pkg.setTrackingNumber(generateTrackingNumber());
        }
        if (pkg.getStatus() == null) {
            pkg.setStatus(PackageStatus.REGISTERED);
        }
        return packageRepository.save(pkg);
    }

    public String generateTrackingNumber() {
        long count = packageRepository.count() + 1;
        int year = Year.now().getValue();
        return String.format("UDN-%d-%04d", year, count);
    }

    public List<Package> findAllPackages() {
        return packageRepository.findAll();
    }

    public Optional<Package> findById(Long id) {
        return packageRepository.findById(id);
    }

    public Optional<Package> findByTrackingNumber(String trackingNumber) {
        return packageRepository.findByTrackingNumber(trackingNumber.trim());
    }

    public List<Package> findByCustomer(User customer) {
        return packageRepository.findByCustomerOrderByIdDesc(customer);
    }

    public List<Package> searchPackages(String query) {
        if (query == null || query.trim().isEmpty()) {
            return packageRepository.findAll();
        }
        return packageRepository.searchPackages(query.trim());
    }

    public List<Package> filterByStatus(PackageStatus status) {
        if (status == null) {
            return packageRepository.findAll();
        }
        return packageRepository.findByStatus(status);
    }

    public Package updatePackage(Long id, Package updatedPkg) {
        Package existing = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + id));
        
        existing.setPackageName(updatedPkg.getPackageName());
        existing.setDescription(updatedPkg.getDescription());
        existing.setWeight(updatedPkg.getWeight());
        existing.setSource(updatedPkg.getSource());
        existing.setDestination(updatedPkg.getDestination());
        if (updatedPkg.getStatus() != null) {
            existing.setStatus(updatedPkg.getStatus());
        }
        return packageRepository.save(existing);
    }

    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }

    public long countTotalPackages() {
        return packageRepository.count();
    }
}
