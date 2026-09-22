package com.cllg.user_service.entity;

import com.cllg.user_service.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.node.StringNode;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_user_id")
    private Long authUserId;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false,length=50)
    private String email;
    @Column(length = 20)
    private String mobile;
    @Column(name = "roll_number", nullable = false, length = 50)
    private String rollNumber;
    @Column(name = "department_id",nullable = false)
    private Long departmentId;
    @Column(nullable = false)
    private Integer year;
    @Column(name = "profile_image", length = 500)
    private String profileImage;
    @Column(length = 1000)
    private String bio;
    @Column(length = 200)
    private String github;
    @Column(length = 500)
    private String linkedin;

    @Enumerated(EnumType.STRING)
    @Column(name="account_status", nullable = false, length = 30)
    @Builder.Default
    private AccountStatus accountStatus=AccountStatus.NOT_REGISTER;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }



}
