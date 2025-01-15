package com.jvnlee.myride.common.service;

import com.jvnlee.myride.driver.dto.DriverLocationDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final BlockingQueue<DriverLocationDto> driverLocationQueue = new LinkedBlockingQueue<>();

    private final RedisTemplate<String, String> redisTemplate;

    public void putDriverLocation(DriverLocationDto driverLocationDto) {
        try {
            driverLocationQueue.put(driverLocationDto);

            log.info("Driver location offered to queue: {}", driverLocationDto);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for driver location to be offered", e);
        }
    }

    public DriverLocationDto takeDriverLocation() throws InterruptedException {
        DriverLocationDto polled = driverLocationQueue.take();

        log.info("Driver location polled from queue: {}", polled);

        return polled;
    }

    public void updateDriverLocation() {
        while (true) {
            try {
                DriverLocationDto driverLocationDto = takeDriverLocation();

                String driverLocationsKey = "driver:locations";
                Long driverId = driverLocationDto.getDriverId();

                Long result = redisTemplate.opsForGeo().add(
                        driverLocationsKey,
                        new Point(driverLocationDto.getLongitude(), driverLocationDto.getLatitude()),
                        driverId.toString()
                );

                if (result != null && result > 0) {
                    log.info("Driver location GeoHash updated to Redis: Driver ID = {}", driverId);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("UpdateLocationThread interrupted", e);
            }
        }
    }

    @PostConstruct
    public void initLocationUpdateThread() {
        Thread locationUpdateThread = new Thread(this::updateDriverLocation);

        locationUpdateThread.setDaemon(true);
        locationUpdateThread.start();
    }

}
