package com.cllg.mentor_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifyRequest {

    @NotNull(
            message = "Mentor ID is required"
    )
    private Long mentorId;

    @NotBlank(
            message = "Razorpay order ID is required"
    )
    private String razorpayOrderId;

    @NotBlank(
            message = "Razorpay payment ID is required"
    )
    private String razorpayPaymentId;

    @NotBlank(
            message = "Razorpay signature is required"
    )
    private String razorpaySignature;
}