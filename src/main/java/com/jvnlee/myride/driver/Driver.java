package com.jvnlee.myride.driver;

import com.jvnlee.myride.vehicle.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    public enum DriverStatus {
        AVAILABLE,
        ASSIGNED,
        UNAVAILABLE
    }

    @Builder
    private Driver(String name, Vehicle vehicle) {
        this.name = name;
        this.vehicle = vehicle;
        this.status = DriverStatus.AVAILABLE;
    }

    public void changeStatus(DriverStatus status) {
        this.status = status;
    }

}
