package com.jvnlee.myride.trip.service;

import com.jvnlee.myride.driver.Driver;
import com.jvnlee.myride.driver.repository.DriverRepository;
import com.jvnlee.myride.driver.service.DriverLocationService;
import com.jvnlee.myride.exception.DriverNotFoundException;
import com.jvnlee.myride.exception.TripNotFoundException;
import com.jvnlee.myride.trip.Trip;
import com.jvnlee.myride.trip.event.TripDriverAssignedEvent;
import com.jvnlee.myride.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TripDriverAssignmentService {

    private final DriverLocationService driverLocationService;

    private final TripRepository tripRepository;

    private final DriverRepository driverRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void assignDriver(long tripId, double pickupLatitude, double pickupLongitude) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        Long driverId = driverLocationService.findClosestDriver(pickupLatitude, pickupLongitude);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(DriverNotFoundException::new);

        trip.assignDriver(driver);

        applicationEventPublisher.publishEvent(new TripDriverAssignedEvent(tripId, driverId));
    }

}
