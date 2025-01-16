package com.jvnlee.myride.trip.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TripDriverAssignedEvent {

    private long tripId;

    private long driverId;

}
