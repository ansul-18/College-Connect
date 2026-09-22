package com.cllg.chat_service.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "conversation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_mentor_conversation",
                        columnNames = {
                                "student_id",
                                "mentor_id"
                        }
                )
        }
)
public class Conversation {

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
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastMessageAt;

    @PrePersist
    private void onCreate() {

        this.createdAt =
                LocalDateTime.now();
    }
}