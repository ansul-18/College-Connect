package com.cllg.community_service.service;



import com.cllg.community_service.dto.request.PostRequest;
import com.cllg.community_service.dto.response.PostResponse;
import com.cllg.community_service.enums.PostCategory;

import java.util.List;

public interface PostService {

    PostResponse create(
            PostRequest request
    );

    PostResponse getById(
            Long id
    );

    List<PostResponse> getAll();

    List<PostResponse> getByCategory(
            PostCategory category
    );

    List<PostResponse> search(
            String keyword
    );

    List<PostResponse> getByAuthor(
            Long authorId
    );

    void delete(
            Long id
    );
}