package com.jvnlee.myride.driver.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class DriverLocationDto {

    private Long driverId;

    private double latitude;

    private double longitude;

}
