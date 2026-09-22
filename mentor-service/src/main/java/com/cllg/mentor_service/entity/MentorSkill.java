package com.cllg.mentor_service.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "mentor_skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_mentor_skill",
                        columnNames = {
                                "mentor_id",
                                "skill_name"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "mentor_id",
            nullable = false
    )
    private Long mentorId;

    @Column(
            name = "skill_name",
            nullable = false,
            length = 100
    )
    private String skillName;
}