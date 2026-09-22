package com.cllg.community_service.service;


import com.cllg.community_service.dto.response.PostResponse;
import com.cllg.community_service.dto.response.TagResponse;

import java.util.List;

public interface TagService {

    List<TagResponse> getAllTags();

    List<PostResponse> getPostsByTag(
            String tagName
    );
}