package com.cllg.college_service.entity;

import com.cllg.college_service.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor@Builder
@Entity
@Table(name = "event_registrations",uniqueConstraints = {@UniqueConstraint(name = "uk_event_student",columnNames={
        "event_id","student_id"})})
public class EventRegistration {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id",nullable = false)
    private Long eventId;
    @Column(name = "student_id",nullable = false)
    private Long studentId;
    @Column(nullable = false,updatable = false)
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    @Builder.Default
    private RegistrationStatus status = RegistrationStatus.REGISTERED;

    @PrePersist
    protected void onCreate(){
        this.registeredAt = LocalDateTime.now();
    }

}
