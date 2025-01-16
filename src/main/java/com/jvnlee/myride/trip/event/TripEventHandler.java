package com.jvnlee.myride.trip.event;

import com.jvnlee.myride.rider.broadcaster.RiderWebSocketBroadcaster;
import com.jvnlee.myride.trip.service.TripDriverAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TripEventHandler {

    private final TripDriverAssignmentService tripDriverAssignmentService;

    private final RiderWebSocketBroadcaster riderWebSocketBroadcaster;

    @TransactionalEventListener
    public void handleTripCreated(TripCreatedEvent event) {
        tripDriverAssignmentService.assignDriver(event.getTripId(), event.getPickupLatitude(), event.getPickupLongitude());
    }

    @TransactionalEventListener
    public void handleTripDriverAssigned(TripDriverAssignedEvent event) {
        riderWebSocketBroadcaster.broadcastDriverLocation(event.getTripId(), event.getDriverId());
    }

}
