package com.jvnlee.myride.rider.broadcaster;

import com.jvnlee.myride.driver.dto.DriverLocationDto;
import com.jvnlee.myride.driver.service.DriverLocationService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RiderWebSocketBroadcaster {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final DriverLocationService driverLocationService;

    private final Map<Long, ScheduledFuture<?>> activeLocationBroadcasts = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final int LOCATION_UPDATE_INTERVAL = 3;

    public void broadcastDriverLocation(long tripId, long driverId) {
        ScheduledFuture<?> broadcastTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                DriverLocationDto driverLocation = driverLocationService.getDriverLocation(driverId);
                String destination = String.format("/queue/trips/%d", tripId);

                simpMessagingTemplate.convertAndSend(destination, driverLocation);

                log.info("Broadcast driver location - Trip ID: {}, Driver ID: {}, Latitude: {}, Longitude: {}",
                        tripId, driverId, driverLocation.getLatitude(), driverLocation.getLongitude());

            } catch (Exception e) {
                log.error("Error broadcasting driver location - Trip ID: {}, Driver ID: {}", tripId, driverId, e);
            }
        }, 0, LOCATION_UPDATE_INTERVAL, TimeUnit.SECONDS);

        activeLocationBroadcasts.put(tripId, broadcastTask);
    }

    public void stopBroadcasting(long tripId) {
        ScheduledFuture<?> existingTask = activeLocationBroadcasts.remove(tripId);

        if (existingTask != null) {
            existingTask.cancel(false);
            log.info("Stopped location broadcast for Trip ID: {}", tripId);
        }
    }

    @PreDestroy
    public void cleanup() {
        activeLocationBroadcasts.values().forEach(task -> task.cancel(false));
        scheduler.shutdown();
    }

}
