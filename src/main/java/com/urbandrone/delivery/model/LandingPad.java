package com.urbandrone.delivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "landing_pads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandingPad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pad name is required")
    @Column(nullable = false, unique = true)
    private String padName;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LandingPadStatus status;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = LandingPadStatus.AVAILABLE;
        }
    }
}
