package com.jvnlee.myride.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name")
    private ModelName modelName;

    @Column(name = "license_plate_number", unique = true)
    private String licensePlateNumber;

    public enum ModelName {
        K5,
        IONIQ5
    }

    @Builder
    private Vehicle(ModelName modelName, String licensePlateNumber) {
        this.modelName = modelName;
        this.licensePlateNumber = licensePlateNumber;
    }

}
