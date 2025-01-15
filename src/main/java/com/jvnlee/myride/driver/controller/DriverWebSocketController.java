package com.jvnlee.myride.driver.controller;

import com.jvnlee.myride.driver.service.DriverLocationService;
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

    private final DriverLocationService driverLocationService;

    @MessageMapping("/location/driver/update")
    public void processLocationUpdate(@Payload DriverLocationDto driverLocationDto) {
        log.info(
                "Received location update: Driver ID = {}, Latitude = {}, Longitude = {}",
                driverLocationDto.getDriverId(),
                driverLocationDto.getLatitude(),
                driverLocationDto.getLongitude()
        );

        driverLocationService.putDriverLocation(driverLocationDto);
    }

}
