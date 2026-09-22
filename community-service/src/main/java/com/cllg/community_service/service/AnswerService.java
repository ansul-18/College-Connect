package com.cllg.community_service.service;


import com.cllg.community_service.dto.request.AnswerRequest;
import com.cllg.community_service.dto.response.AnswerResponse;

import java.util.List;

public interface AnswerService {

    AnswerResponse create(
            AnswerRequest request
    );

    List<AnswerResponse> getByPost(
            Long postId
    );

    void delete(
            Long id
    );
}