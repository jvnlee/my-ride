package com.jvnlee.myride.driver.service;

import com.jvnlee.myride.driver.dto.DriverLocationDto;
import com.jvnlee.myride.exception.FailedToAssignDriverException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverLocationService {

    private final BlockingQueue<DriverLocationDto> driverLocationQueue = new LinkedBlockingQueue<>();

    private final RedisTemplate<String, String> redisTemplate;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final String DRIVER_LOCATIONS_KEY = "driver:locations";

    private static final double DEFAULT_SEARCH_RADIUS_KM = 10.0;

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
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DriverLocationDto driverLocationDto = takeDriverLocation();
                Long driverId = driverLocationDto.getDriverId();
                Long result = redisTemplate.opsForGeo().add(
                        DRIVER_LOCATIONS_KEY,
                        new Point(driverLocationDto.getLongitude(), driverLocationDto.getLatitude()),
                        driverId.toString()
                );

                if (result != null && result > 0) {
                    log.info("Driver location GeoHash updated to Redis: Driver ID = {}", driverId);
                }
            } catch (InterruptedException e) {
                log.warn("UpdateDriverLocationThread interrupted, shutting down gracefully...");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error updating driver location: {}", e.getMessage(), e);
            }
        }
    }

    public Long findClosestDriver(double longitude, double latitude) {
        Point pickupLocation = new Point(longitude, latitude);
        Distance radius = new Distance(DEFAULT_SEARCH_RADIUS_KM, Metrics.KILOMETERS);

        GeoResults<GeoLocation<String>> geoResults = redisTemplate.opsForGeo().radius(
                DRIVER_LOCATIONS_KEY,
                new Circle(pickupLocation, radius),
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()
                        .includeCoordinates()
                        .sortAscending()
                        .limit(1)
        );

        if (geoResults == null || geoResults.getContent().isEmpty()) {
            throw new FailedToAssignDriverException();
        }

        String closestDriverId = geoResults.getContent().get(0).getContent().getName();
        log.info(
                "Closest driver ID: {}\nRequested pickup location: lat: {} lng: {}",
                closestDriverId, latitude, longitude
        );

        return Long.parseLong(closestDriverId);
    }

    @PostConstruct
    public void initExecutorService() {
        log.info("Initiating executor service...");
        executorService.submit(this::updateDriverLocation);
    }

    @PreDestroy
    public void shutdownExecutorService() {
        log.info("Shutting down executor service...");
        executorService.shutdown();
    }

}
