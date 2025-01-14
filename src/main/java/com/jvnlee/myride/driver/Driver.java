package com.jvnlee.myride.driver;

import com.jvnlee.myride.common.GeoLocation;
import com.jvnlee.myride.trip.Trip;
import com.jvnlee.myride.vehicle.Vehicle;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Embedded
    private GeoLocation currentLocation;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @Builder.Default
    private List<Trip> trips = new ArrayList<>();

    public enum DriverStatus {
        AVAILABLE,
        ASSIGNED,
        UNAVAILABLE
    }

}
