package com.jvnlee.myride.driver.controller;

import com.jvnlee.myride.common.service.LocationService;
import com.jvnlee.myride.driver.dto.DriverLocationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DriverWebSocketController {

    private final LocationService locationService;

    @MessageMapping("/location/driver/update")
    public void processLocationUpdate(@Payload DriverLocationDto driverLocationDto) {
        log.info(
                "Received location update: Driver ID = {}, Latitude = {}, Longitude = {}",
                driverLocationDto.getDriverId(),
                driverLocationDto.getLatitude(),
                driverLocationDto.getLongitude()
        );

        locationService.putDriverLocation(driverLocationDto);
    }

}
