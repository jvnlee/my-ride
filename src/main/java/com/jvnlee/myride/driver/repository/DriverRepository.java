package com.jvnlee.myride.driver.repository;

import com.jvnlee.myride.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
