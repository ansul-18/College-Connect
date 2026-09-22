package com.cllg.mentor_service.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentOrderRequest {

    @NotNull(
            message = "Mentor ID is required"
    )
    private Long mentorId;
}