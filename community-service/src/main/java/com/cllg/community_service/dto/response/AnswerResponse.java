package com.cllg.community_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponse {

    private Long id;

    private Long postId;

    private Long authorId;

    private String authorName;

    private String authorProfileImage;

    private String content;

    private LocalDateTime createdAt;
}