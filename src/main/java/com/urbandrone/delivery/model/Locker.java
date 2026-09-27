package com.urbandrone.delivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "lockers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Locker number is required")
    @Column(nullable = false, unique = true)
    private String lockerNumber;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotBlank(message = "Size is required")
    @Column(nullable = false)
    private String size; // e.g. SMALL, MEDIUM, LARGE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LockerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package currentPackage;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = LockerStatus.AVAILABLE;
        }
    }
}
