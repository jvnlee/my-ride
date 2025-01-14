package com.jvnlee.myride.driver;

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
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    public enum DriverStatus {
        AVAILABLE,
        ASSIGNED,
        UNAVAILABLE
    }

    @Builder
    private Driver(String name) {
        this.name = name;
        this.status = DriverStatus.AVAILABLE;
    }

    public void changeStatus(DriverStatus status) {
        this.status = status;
    }

}
