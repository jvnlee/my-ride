package com.jvnlee.myride.trip;

import com.jvnlee.myride.driver.Driver;
import com.jvnlee.myride.rider.Rider;
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

    @Column(name = "pickup_latitude", nullable = false)
    private double pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false)
    private double pickupLongitude;

    @Column(name = "dropoff_latitude", nullable = false)
    private double dropoffLatitude;

    @Column(name = "dropoff_longitude", nullable = false)
    private double dropoffLongitude;

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
    private Trip(Rider rider, double pickupLatitude, double pickupLongitude, double dropoffLatitude, double dropoffLongitude) {
        this.rider = rider;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.dropoffLatitude = dropoffLatitude;
        this.dropoffLongitude = dropoffLongitude;
        this.status = TripStatus.REQUESTED;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
    }

    public void startTrip() {
        this.pickupTime = LocalDateTime.now();
        this.status = TripStatus.IN_PROGRESS;
    }

    public void finishTrip() {
        this.dropoffTime = LocalDateTime.now();
        this.status = TripStatus.COMPLETED;
    }

}
