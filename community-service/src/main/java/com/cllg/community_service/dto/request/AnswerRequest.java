package com.cllg.community_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AnswerRequest {

    @NotNull(message = "Post id is required")
    private Long postId;


    @NotNull(message = "Author id is required")
    private Long authorId;


    @NotBlank(message = "Answer cannot be empty")
    private String content;
}