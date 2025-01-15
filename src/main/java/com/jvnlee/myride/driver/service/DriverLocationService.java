package com.jvnlee.myride.driver.service;

import com.jvnlee.myride.driver.dto.DriverLocationDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
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
            } catch (InterruptedException e) {
                log.warn("UpdateDriverLocationThread interrupted, shutting down gracefully...");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error updating driver location: {}", e.getMessage(), e);
            }
        }
    }

    @PostConstruct
    public void initDriverLocationUpdateThread() {
        executorService.submit(this::updateDriverLocation);
    }

    @PreDestroy
    public void shutdownExecutorService() {
        log.info("Shutting down executor service...");
        executorService.shutdown();
    }

}
