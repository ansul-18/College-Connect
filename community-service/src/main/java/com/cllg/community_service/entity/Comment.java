package com.cllg.community_service.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "post_id",
            nullable = false
    )
    private Long postId;


    @Column(
            name = "author_id",
            nullable = false
    )
    private Long authorId;


    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;


    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {

        this.createdAt =
                LocalDateTime.now();
    }
}