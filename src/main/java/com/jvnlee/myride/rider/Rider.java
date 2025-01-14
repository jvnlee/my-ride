package com.jvnlee.myride.rider;

import com.jvnlee.myride.common.GeoLocation;
import com.jvnlee.myride.trip.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private GeoLocation currentLocation;

    @Enumerated(EnumType.STRING)
    private RiderStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rider")
    private List<Trip> trips = new ArrayList<>();

    public enum RiderStatus {
        ON_TRIP,
        NOT_ON_TRIP
    }

    @Builder
    private Rider(String name) {
        this.name = name;
        this.status = RiderStatus.NOT_ON_TRIP;
    }

}
