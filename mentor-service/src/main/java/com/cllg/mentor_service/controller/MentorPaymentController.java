package com.cllg.mentor_service.controller;

import com.cllg.mentor_service.dto.request.PaymentOrderRequest;
import com.cllg.mentor_service.dto.request.PaymentVerifyRequest;

import com.cllg.mentor_service.dto.response.MentorAccessResponse;
import com.cllg.mentor_service.dto.response.PaymentOrderResponse;

import com.cllg.mentor_service.service.RazorpayService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentors/payment")
@RequiredArgsConstructor
public class MentorPaymentController {

    private final RazorpayService razorpayService;

    @PostMapping("/order")
    public ResponseEntity<PaymentOrderResponse>
    createOrder(
            @Valid
            @RequestBody
            PaymentOrderRequest request,

            Authentication authentication
    ) {

        Long studentId =
                getStudentId(authentication);

        return ResponseEntity.ok(
                razorpayService.createOrder(
                        studentId,
                        request
                )
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<MentorAccessResponse>
    verifyPayment(
            @Valid
            @RequestBody
            PaymentVerifyRequest request,

            Authentication authentication
    ) {

        Long studentId =
                getStudentId(authentication);

        return ResponseEntity.ok(
                razorpayService.verifyPayment(
                        studentId,
                        request
                )
        );
    }

    private Long getStudentId(
            Authentication authentication
    ) {

        return Long.parseLong(
                authentication.getName()
        );
    }
}