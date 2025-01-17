package com.jvnlee.myride.payment.service;

import com.jvnlee.myride.exception.PaymentNotFoundException;
import com.jvnlee.myride.payment.Payment;
import com.jvnlee.myride.payment.Payment.PaymentStatus;
import com.jvnlee.myride.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void completePayment(long tripId, int price) {
        Payment payment = paymentRepository.findByTripId(tripId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.insertPrice(price);

        // ... 결제 처리 ...

        payment.changeStatus(PaymentStatus.COMPLETED);

        log.info("Payment completed for Trip ID {}, Price: {}", tripId, price);
    }

}
