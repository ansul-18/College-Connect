package com.cllg.mentor_service.entity;

import com.cllg.mentor_service.enums.MentorAccessStatus;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "mentor_access",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_mentor_access",
                        columnNames = {
                                "student_id",
                                "mentor_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "student_id",
            nullable = false
    )
    private Long studentId;

    @Column(
            name = "mentor_id",
            nullable = false
    )
    private Long mentorId;

    @Column(
            name = "razorpay_order_id",
            unique = true,
            length = 100
    )
    private String razorpayOrderId;

    @Column(
            name = "payment_id",
            unique = true,
            length = 100
    )
    private String paymentId;

    @Column(
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    @Builder.Default
    private MentorAccessStatus status =
            MentorAccessStatus.FAILED;

    @Column(
            name = "purchased_at"
    )
    private LocalDateTime purchasedAt;
}