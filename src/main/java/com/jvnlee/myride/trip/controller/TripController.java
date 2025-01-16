package com.jvnlee.myride.trip.controller;

import com.jvnlee.myride.trip.dto.CreateTripRequestDto;
import com.jvnlee.myride.trip.dto.CreateTripResponseDto;
import com.jvnlee.myride.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

}
