package com.cllg.community_service.service;



import com.cllg.community_service.dto.request.CommentRequest;
import com.cllg.community_service.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse create(
            CommentRequest request
    );

    List<CommentResponse> getByPost(
            Long postId
    );

    void delete(
            Long id
    );
}