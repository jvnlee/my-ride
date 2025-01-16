package com.jvnlee.myride.trip.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TripCreatedEvent {

    private long tripId;

    private double pickupLatitude;

    private double pickupLongitude;

}
