package com.cllg.mentor_service.dto.response;

import com.cllg.mentor_service.enums.MentorAccessStatus;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorAccessResponse {

    private Long id;

    private Long studentId;

    private Long mentorId;

    private String razorpayOrderId;

    private String paymentId;

    private BigDecimal amount;

    private MentorAccessStatus status;

    private LocalDateTime purchasedAt;

    private Boolean chatAllowed;
}