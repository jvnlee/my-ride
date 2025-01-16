package com.jvnlee.myride.trip.event;

import com.jvnlee.myride.rider.service.RiderBroadcastingService;
import com.jvnlee.myride.trip.service.TripDriverAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TripEventHandler {

    private final TripDriverAssignmentService tripDriverAssignmentService;

    private final RiderBroadcastingService riderBroadcastingService;

    @TransactionalEventListener
    public void handleTripCreated(TripCreatedEvent event) {
        tripDriverAssignmentService.assignDriver(event.getTripId(), event.getPickupLatitude(), event.getPickupLongitude());
    }

    @TransactionalEventListener
    public void handleTripDriverAssigned(TripDriverAssignedEvent event) {
        riderBroadcastingService.broadcastDriverLocation(event.getTripId(), event.getDriverId());
    }

}
