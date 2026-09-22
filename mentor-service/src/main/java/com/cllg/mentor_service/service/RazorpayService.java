package com.cllg.mentor_service.service;

import com.cllg.mentor_service.dto.request.PaymentOrderRequest;
import com.cllg.mentor_service.dto.request.PaymentVerifyRequest;
import com.cllg.mentor_service.dto.response.MentorAccessResponse;
import com.cllg.mentor_service.dto.response.PaymentOrderResponse;

public interface RazorpayService {

    PaymentOrderResponse createOrder(
            Long studentId,
            PaymentOrderRequest request
    );

    MentorAccessResponse verifyPayment(
            Long studentId,
            PaymentVerifyRequest request
    );
}