package com.jvnlee.myride.trip.controller;

import com.jvnlee.myride.trip.dto.CreateTripRequestDto;
import com.jvnlee.myride.trip.dto.CreateTripResponseDto;
import com.jvnlee.myride.trip.dto.FinishTripRequestDto;
import com.jvnlee.myride.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/")
    public ResponseEntity<CreateTripResponseDto> createTrip(@RequestBody CreateTripRequestDto createTripRequestDto) {
        return ResponseEntity.ok(tripService.createTrip(createTripRequestDto));
    }

    @PutMapping("/{tripId}/start")
    public ResponseEntity<Void> startTrip(@PathVariable Long tripId) {
        tripService.startTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tripId}/finish")
    public ResponseEntity<Void> finishTrip(@PathVariable Long tripId, @RequestBody FinishTripRequestDto finishTripRequestDto) {
        tripService.finishTrip(tripId, finishTripRequestDto);
        return ResponseEntity.ok().build();
    }

}
