package com.jvnlee.myride.trip;

import com.jvnlee.myride.payment.Payment;
import com.jvnlee.myride.common.GeoLocation;
import com.jvnlee.myride.driver.Driver;
import com.jvnlee.myride.rider.Rider;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude"))
    })
    private GeoLocation pickupLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "dropoff_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dropoff_longitude"))
    })
    private GeoLocation dropoffLocation;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "dropoff_time")
    private LocalDateTime dropoffTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

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
    private Trip(Rider rider, GeoLocation pickupLocation, GeoLocation dropoffLocation, Payment payment) {
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.payment = payment;
        this.status = TripStatus.REQUESTED;
    }

}
