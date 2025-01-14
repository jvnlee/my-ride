package com.jvnlee.myride.rider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private RiderStatus status;

    public enum RiderStatus {
        ON_TRIP,
        NOT_ON_TRIP
    }

    @Builder
    private Rider(String name) {
        this.name = name;
        this.status = RiderStatus.NOT_ON_TRIP;
    }

    public void changeStatus(RiderStatus status) {
        this.status = status;
    }

}
