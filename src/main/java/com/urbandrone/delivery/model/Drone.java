package com.urbandrone.delivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "drones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Drone name is required")
    @Column(nullable = false, unique = true)
    private String droneName;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    @Column(nullable = false)
    private Double capacity; // Payload capacity in KG

    @NotNull(message = "Battery level is required")
    @Min(value = 0, message = "Battery level cannot be negative")
    @Max(value = 100, message = "Battery level cannot exceed 100")
    @Column(nullable = false)
    private Integer batteryLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneStatus status;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = DroneStatus.AVAILABLE;
        }
        if (this.batteryLevel == null) {
            this.batteryLevel = 100;
        }
    }
}
