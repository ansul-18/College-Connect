package com.cllg.college_service.entity;

import com.cllg.college_service.enums.EventCategory;
import com.cllg.college_service.enums.EventStatus;
import com.cllg.college_service.enums.TargetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Struct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "department_id")
    private Long departmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private EventCategory eventCategory;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(length = 200)
    private String venue;

    @Builder.Default
    @Column(nullable = false)
    private boolean registrationRequired=false;

    private Integer registrationLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus eventStatus = EventStatus.UPCOMING;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        this.createdAt = now;

        this.updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {

        this.updatedAt =
                LocalDateTime.now();
    }
}
