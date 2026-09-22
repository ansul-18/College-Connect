package com.cllg.mentor_service.service;

import com.cllg.mentor_service.dto.response.MentorAccessResponse;

import com.cllg.mentor_service.entity.MentorAccess;
import com.cllg.mentor_service.entity.MentorProfile;

import com.cllg.mentor_service.enums.MentorAccessStatus;
import com.cllg.mentor_service.enums.MentorType;

import com.cllg.mentor_service.exception.ResourceNotFoundException;

import com.cllg.mentor_service.repository.MentorAccessRepository;
import com.cllg.mentor_service.repository.MentorProfileRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MentorAccessServiceImpl
        implements MentorAccessService {

    private final MentorAccessRepository
            mentorAccessRepository;

    private final MentorProfileRepository
            mentorProfileRepository;

    @Override
    public MentorAccessResponse getAccess(
            Long studentId,
            Long mentorId
    ) {

        MentorProfile mentor =
                mentorProfileRepository
                        .findById(mentorId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Mentor not found"
                                        )
                        );

        if (!Boolean.TRUE.equals(
                mentor.getActive()
        )) {

            throw new IllegalArgumentException(
                    "Mentor is not active"
            );
        }

        /*
         * FREE mentor
         */
        if (mentor.getMentorType()
                == MentorType.FREE) {

            return MentorAccessResponse
                    .builder()
                    .studentId(studentId)
                    .mentorId(mentorId)
                    .amount(BigDecimal.ZERO)
                    .status(
                            MentorAccessStatus.ACTIVE
                    )
                    .chatAllowed(true)
                    .build();
        }

        MentorAccess access =
                mentorAccessRepository
                        .findByStudentIdAndMentorId(
                                studentId,
                                mentorId
                        )
                        .orElse(null);

        if (access == null) {

            return MentorAccessResponse
                    .builder()
                    .studentId(studentId)
                    .mentorId(mentorId)
                    .amount(
                            mentor.getPrice()
                    )
                    .status(
                            MentorAccessStatus.FAILED
                    )
                    .chatAllowed(false)
                    .build();
        }

        return map(access);
    }



    @Override
    public boolean canChat(
            Long studentId,
            Long mentorId
    ) {

        MentorProfile mentor =
                mentorProfileRepository
                        .findById(mentorId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Mentor not found"
                                        )
                        );

        if (!Boolean.TRUE.equals(
                mentor.getActive()
        )) {

            return false;
        }

        /*
         * FREE mentor
         */
        if (mentor.getMentorType()
                == MentorType.FREE) {

            return true;
        }

        /*
         * PAID mentor
         */
        return mentorAccessRepository
                .existsByStudentIdAndMentorIdAndStatus(
                        studentId,
                        mentorId,
                        MentorAccessStatus.ACTIVE
                );
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