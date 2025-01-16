package com.jvnlee.myride.trip.service;

import com.jvnlee.myride.exception.RiderNotFoundException;
import com.jvnlee.myride.payment.Payment;
import com.jvnlee.myride.payment.repository.PaymentRepository;
import com.jvnlee.myride.rider.Rider;
import com.jvnlee.myride.rider.repository.RiderRepository;
import com.jvnlee.myride.trip.Trip;
import com.jvnlee.myride.trip.dto.CreateTripRequestDto;
import com.jvnlee.myride.trip.dto.CreateTripResponseDto;
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
                        .method(createTripRequestDto.getPaymentMethod())
                        .build()
        );

        // 이벤트 발행
        // TripCreatedEvent -> eventhandler -> findClosestDriver
        // -> Assign (Trip에 Driver 지정, TripStatus IN_PROGRESS 될 때까지 Rider에게 Driver 위치 실시간 업데이트)
        log.info("Requested Trip created with ID {}", tripId);
        applicationEventPublisher.publishEvent(new TripCreatedEvent(tripId, pickupLatitude, pickupLongitude));

        return new CreateTripResponseDto(tripId);
    }

}
