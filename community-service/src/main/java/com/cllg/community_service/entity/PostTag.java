package com.cllg.community_service.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "post_tags",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_post_tag",
                        columnNames = {
                                "post_id",
                                "tag_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "post_id",
            nullable = false
    )
    private Long postId;


    @Column(
            name = "tag_id",
            nullable = false
    )
    private Long tagId;
}