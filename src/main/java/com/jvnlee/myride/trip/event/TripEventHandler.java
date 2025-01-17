package com.jvnlee.myride.trip.event;

import com.jvnlee.myride.payment.service.PaymentService;
import com.jvnlee.myride.rider.broadcaster.RiderWebSocketBroadcaster;
import com.jvnlee.myride.trip.service.TripDriverAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TripEventHandler {

    private final TripDriverAssignmentService tripDriverAssignmentService;

    private final RiderWebSocketBroadcaster riderWebSocketBroadcaster;

    private final PaymentService paymentService;

    @Async
    @TransactionalEventListener
    public void handleTripCreated(TripCreatedEvent event) {
        tripDriverAssignmentService.assignDriver(event.getTripId(), event.getPickupLatitude(), event.getPickupLongitude());
    }

    @TransactionalEventListener
    public void handleTripDriverAssigned(TripDriverAssignedEvent event) {
        riderWebSocketBroadcaster.broadcastDriverLocation(event.getTripId(), event.getDriverId());
    }

    @EventListener
    public void handleTripCompleted(TripCompletedEvent event) {
        riderWebSocketBroadcaster.stopBroadcasting(event.getTripId());

        paymentService.completePayment(event.getTripId(), event.getPrice());
    }

}
