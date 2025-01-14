package com.jvnlee.myride.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "method")
    private PaymentMethod method;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private PaymentStatus status;

    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        ONLINE
    }

    public enum PaymentStatus {
        COMPLETED,
        FAILED
    }

    @Builder
    private Payment(PaymentMethod method) {
        this.method = method;
        this.status = PaymentStatus.PENDING;
    }

}
