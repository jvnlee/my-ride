package com.jvnlee.myride.trip;

import com.jvnlee.myride.driver.Driver;
import com.jvnlee.myride.rider.Rider;
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
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "dropoff_location", nullable = false)
    private String dropoffLocation;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "dropoff_time")
    private LocalDateTime dropoffTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    public enum TripStatus {
        REQUESTED,
        DRIVER_ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELED
    }

    @Builder
    private Trip(Driver driver, Rider rider, Vehicle vehicle, String pickupLocation, String dropoffLocation) {
        this.driver = driver;
        this.rider = rider;
        this.vehicle = vehicle;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = TripStatus.REQUESTED;
    }

    public void startTrip() {
        this.pickupTime = LocalDateTime.now();
        this.status = TripStatus.IN_PROGRESS;
    }

    public void completeTrip() {
        this.dropoffTime = LocalDateTime.now();
        this.status = TripStatus.COMPLETED;
    }

}
