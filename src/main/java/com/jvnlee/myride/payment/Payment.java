package com.jvnlee.myride.payment;

import com.jvnlee.myride.rider.Rider;
import com.jvnlee.myride.trip.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false)
    private PaymentStatus status;

    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        ONLINE
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    @Builder
    private Payment(int price, Trip trip, Rider rider, PaymentMethod method) {
        this.price = price;
        this.trip = trip;
        this.rider = rider;
        this.method = method;
        this.status = PaymentStatus.PENDING;
    }

    public void changeStatus(PaymentStatus status) {
        this.status = status;
    }

}
