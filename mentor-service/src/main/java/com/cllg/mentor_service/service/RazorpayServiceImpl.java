package com.cllg.mentor_service.service;

import com.cllg.mentor_service.dto.request.PaymentOrderRequest;
import com.cllg.mentor_service.dto.request.PaymentVerifyRequest;

import com.cllg.mentor_service.dto.response.MentorAccessResponse;
import com.cllg.mentor_service.dto.response.PaymentOrderResponse;

import com.cllg.mentor_service.entity.MentorAccess;
import com.cllg.mentor_service.entity.MentorProfile;

import com.cllg.mentor_service.enums.MentorAccessStatus;
import com.cllg.mentor_service.enums.MentorType;

import com.cllg.mentor_service.exception.ResourceNotFoundException;

import com.cllg.mentor_service.repository.MentorAccessRepository;
import com.cllg.mentor_service.repository.MentorProfileRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RazorpayServiceImpl
        implements RazorpayService {

    private final RazorpayClient razorpayClient;

    private final MentorProfileRepository
            mentorProfileRepository;

    private final MentorAccessRepository
            mentorAccessRepository;

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    @Override
    public PaymentOrderResponse createOrder(
            Long studentId,
            PaymentOrderRequest request
    ) {

        MentorProfile mentor =
                mentorProfileRepository
                        .findById(
                                request.getMentorId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Mentor not found"
                                        )
                        );

        /*
         * Mentor must be active.
         */
        if (!Boolean.TRUE.equals(
                mentor.getActive()
        )) {

            throw new IllegalArgumentException(
                    "Mentor is not active"
            );
        }

        /*
         * Only PAID mentor can use Razorpay.
         */
        if (mentor.getMentorType()
                != MentorType.PAID) {

            throw new IllegalArgumentException(
                    "This mentor is free"
            );
        }

        /*
         * Validate price.
         */
        if (mentor.getPrice() == null ||
                mentor.getPrice()
                        .compareTo(
                                BigDecimal.ZERO
                        ) <= 0) {

            throw new IllegalArgumentException(
                    "Mentor price is invalid"
            );
        }

        /*
         * Already purchased?
         */
        boolean alreadyActive =
                mentorAccessRepository
                        .existsByStudentIdAndMentorIdAndStatus(
                                studentId,
                                request.getMentorId(),
                                MentorAccessStatus.ACTIVE
                        );

        if (alreadyActive) {

            throw new IllegalArgumentException(
                    "Student already has active access"
            );
        }

        long amountInPaise =
                mentor.getPrice()
                        .multiply(
                                BigDecimal.valueOf(100)
                        )
                        .longValueExact();

        String receipt =
                "mentor_"
                        + request.getMentorId()
                        + "_student_"
                        + studentId
                        + "_"
                        + System.currentTimeMillis();

        try {

            JSONObject options =
                    new JSONObject();

            options.put(
                    "amount",
                    amountInPaise
            );

            options.put(
                    "currency",
                    "INR"
            );

            options.put(
                    "receipt",
                    receipt
            );

            Order order =
                    razorpayClient.orders.create(
                            options
                    );

            String orderId =
                    order.get("id")
                            .toString();

            /*
             * Find existing failed access,
             * otherwise create new access.
             */
            MentorAccess access =
                    mentorAccessRepository
                            .findByStudentIdAndMentorId(
                                    studentId,
                                    request.getMentorId()
                            )
                            .orElseGet(
                                    MentorAccess::new
                            );

            access.setStudentId(
                    studentId
            );

            access.setMentorId(
                    request.getMentorId()
            );

            access.setRazorpayOrderId(
                    orderId
            );

            access.setAmount(
                    mentor.getPrice()
            );

            access.setStatus(
                    MentorAccessStatus.FAILED
            );

            access.setPaymentId(
                    null
            );

            access.setPurchasedAt(
                    null
            );

            mentorAccessRepository.save(
                    access
            );

            return PaymentOrderResponse
                    .builder()
                    .orderId(orderId)
                    .amount(amountInPaise)
                    .currency("INR")
                    .mentorId(
                            request.getMentorId()
                    )
                    .studentId(studentId)
                    .keyId(keyId)
                    .build();

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to create Razorpay order",
                    ex
            );
        }
    }

    @Override
    public MentorAccessResponse verifyPayment(
            Long studentId,
            PaymentVerifyRequest request
    ) {

        MentorAccess access =
                mentorAccessRepository
                        .findByRazorpayOrderId(
                                request.getRazorpayOrderId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Razorpay order not found"
                                        )
                        );

        /*
         * Order ownership check.
         */
        if (!access.getStudentId()
                .equals(studentId)) {

            throw new IllegalArgumentException(
                    "This payment does not belong to the student"
            );
        }

        /*
         * Mentor ownership check.
         */
        if (!access.getMentorId()
                .equals(request.getMentorId())) {

            throw new IllegalArgumentException(
                    "Mentor mismatch"
            );
        }

        /*
         * Already ACTIVE?
         */
        if (access.getStatus()
                == MentorAccessStatus.ACTIVE) {

            return map(access);
        }

        try {

            /*
             * Important:
             * Use order ID from our database.
             */
            JSONObject options =
                    new JSONObject();

            options.put(
                    "razorpay_order_id",
                    access.getRazorpayOrderId()
            );

            options.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            options.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean valid =
                    Utils.verifyPaymentSignature(
                            options,
                            keySecret
                    );

            if (!valid) {

                access.setStatus(
                        MentorAccessStatus.FAILED
                );

                mentorAccessRepository.save(
                        access
                );

                throw new IllegalArgumentException(
                        "Invalid Razorpay payment signature"
                );
            }

            /*
             * Successful verification.
             */
            access.setPaymentId(
                    request.getRazorpayPaymentId()
            );

            access.setStatus(
                    MentorAccessStatus.ACTIVE
            );

            access.setPurchasedAt(
                    LocalDateTime.now()
            );

            MentorAccess saved =
                    mentorAccessRepository.save(
                            access
                    );

            return map(saved);

        } catch (IllegalArgumentException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to verify Razorpay payment",
                    ex
            );
        }
    }

    private MentorAccessResponse map(
            MentorAccess access
    ) {

        return MentorAccessResponse
                .builder()

                .id(
                        access.getId()
                )

                .studentId(
                        access.getStudentId()
                )

                .mentorId(
                        access.getMentorId()
                )

                .razorpayOrderId(
                        access.getRazorpayOrderId()
                )

                .paymentId(
                        access.getPaymentId()
                )

                .amount(
                        access.getAmount()
                )

                .status(
                        access.getStatus()
                )

                .purchasedAt(
                        access.getPurchasedAt()
                )

                .chatAllowed(
                        access.getStatus()
                                == MentorAccessStatus.ACTIVE
                )

                .build();
    }
}