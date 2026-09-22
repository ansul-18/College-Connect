package com.cllg.community_service.dto.response;


import com.cllg.community_service.enums.PostCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;

    private Long authorId;

    private String authorName;

    private String authorProfileImage;

    private String title;

    private String content;

    private PostCategory category;

    private List<String> tags;

    private Integer answerCount;

    private Integer commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}