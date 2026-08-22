package com.cllg.user_service.entity;

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
    @GeneratedValue(strategy = "")
    private Long id;
    private Long authUserId;
    private String name;
    private String email;
    private String mobile;
    private String rollNumber;
    private Long departmentId;
    private Integer year;
    private String profileImage;
    private String bio;
    private String github;
    private String linkedin;
    private AccountStatus status;
    private LocalDateTime createdAt;
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
