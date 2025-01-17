package com.jvnlee.myride.trip.service;

import com.jvnlee.myride.driver.Driver.DriverStatus;
import com.jvnlee.myride.exception.IllegalTripStatusException;
import com.jvnlee.myride.exception.RiderNotFoundException;
import com.jvnlee.myride.exception.TripNotFoundException;
import com.jvnlee.myride.payment.Payment;
import com.jvnlee.myride.payment.repository.PaymentRepository;
import com.jvnlee.myride.rider.Rider;
import com.jvnlee.myride.rider.Rider.RiderStatus;
import com.jvnlee.myride.rider.repository.RiderRepository;
import com.jvnlee.myride.trip.Trip;
import com.jvnlee.myride.trip.Trip.TripStatus;
import com.jvnlee.myride.trip.dto.CreateTripRequestDto;
import com.jvnlee.myride.trip.dto.CreateTripResponseDto;
import com.jvnlee.myride.trip.dto.FinishTripRequestDto;
import com.jvnlee.myride.trip.event.TripCompletedEvent;
import com.jvnlee.myride.trip.event.TripCreatedEvent;
import com.jvnlee.myride.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripService {

    private final RiderRepository riderRepository;

    private final TripRepository tripRepository;

    private final PaymentRepository paymentRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public CreateTripResponseDto createTrip(CreateTripRequestDto createTripRequestDto) {
        Rider rider = riderRepository.findById(createTripRequestDto.getRiderId())
                .orElseThrow(RiderNotFoundException::new);

        rider.changeStatus(RiderStatus.ON_TRIP);

        Double pickupLatitude = createTripRequestDto.getPickupLatitude();
        Double pickupLongitude = createTripRequestDto.getPickupLongitude();

        Trip trip = tripRepository.save(
                Trip.builder()
                        .rider(rider)
                        .pickupLatitude(pickupLatitude)
                        .pickupLongitude(pickupLongitude)
                        .dropoffLatitude(createTripRequestDto.getDropoffLatitude())
                        .dropoffLongitude(createTripRequestDto.getDropoffLongitude())
                        .build()
        );

        Long tripId = trip.getId();

        paymentRepository.save(
                Payment.builder()
                        .trip(trip)
                        .rider(rider)
                        .method(createTripRequestDto.getPaymentMethod())
                        .build()
        );

        log.info("Requested Trip created with ID {}", tripId);
        applicationEventPublisher.publishEvent(new TripCreatedEvent(tripId, pickupLatitude, pickupLongitude));

        return new CreateTripResponseDto(tripId);
    }

    @Transactional
    public void startTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        if (trip.getStatus() != TripStatus.REQUESTED) {
            throw new IllegalTripStatusException();
        }

        trip.startTrip();
        log.info("Driver ID {} picked up Rider ID {} for Trip ID {}", trip.getDriver().getId(), trip.getRider().getId(), tripId);
    }

    @Transactional
    public void finishTrip(Long tripId, FinishTripRequestDto finishTripRequestDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new IllegalTripStatusException();
        }

        trip.getDriver().changeStatus(DriverStatus.AVAILABLE);
        trip.getRider().changeStatus(RiderStatus.NOT_ON_TRIP);

        trip.finishTrip();
        log.info("Driver ID {} dropped off Rider ID {} for Trip ID {}", trip.getDriver().getId(), trip.getRider().getId(), tripId);

        applicationEventPublisher.publishEvent(new TripCompletedEvent(trip.getId(), finishTripRequestDto.getPrice()));
    }

}
