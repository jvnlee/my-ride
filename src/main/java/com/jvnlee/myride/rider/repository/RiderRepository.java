package com.jvnlee.myride.rider.repository;

import com.jvnlee.myride.rider.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
