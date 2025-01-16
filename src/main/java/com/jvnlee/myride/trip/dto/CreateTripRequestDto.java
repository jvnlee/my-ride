package com.jvnlee.myride.trip.dto;

import com.jvnlee.myride.payment.Payment.PaymentMethod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreateTripRequestDto {

    private Long riderId; // 인증 시스템이 생기면 빼도 됨

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropoffLatitude;

    private Double dropoffLongitude;

    private PaymentMethod paymentMethod;

}
