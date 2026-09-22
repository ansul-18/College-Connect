package com.cllg.community_service.entity;

import com.cllg.community_service.enums.PostCategory;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "author_id",
            nullable = false
    )
    private Long authorId;


    @Column(
            nullable = false,
            length = 250
    )
    private String title;


    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;


    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 40
    )
    private PostCategory category;


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