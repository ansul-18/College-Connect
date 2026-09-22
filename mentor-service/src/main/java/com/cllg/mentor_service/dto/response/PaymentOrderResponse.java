package com.cllg.mentor_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrderResponse {

    private String orderId;

    private Long amount;

    private String currency;

    private Long mentorId;

    private Long studentId;

    private String keyId;
}