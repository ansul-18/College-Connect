package com.cllg.community_service.dto.request;



import com.cllg.community_service.enums.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {

    @NotNull(message = "Author id is required")
    private Long authorId;


    @NotBlank(message = "Title is required")
    private String title;


    @NotBlank(message = "Content is required")
    private String content;


    @NotNull(message = "Category is required")
    private PostCategory category;


    private List<String> tags;
}