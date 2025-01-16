package com.jvnlee.myride.trip.repository;

import com.jvnlee.myride.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
