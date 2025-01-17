package com.jvnlee.myride.payment.repository;

import com.jvnlee.myride.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTripId(Long tripId);

}
