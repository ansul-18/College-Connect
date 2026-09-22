package com.cllg.community_service.service;


import com.cllg.community_service.dto.response.PostResponse;
import com.cllg.community_service.dto.response.TagResponse;
import com.cllg.community_service.entity.PostTag;
import com.cllg.community_service.entity.Tag;
import com.cllg.community_service.exception.ResourceNotFoundException;
import com.cllg.community_service.repository.PostRepository;
import com.cllg.community_service.repository.PostTagRepository;
import com.cllg.community_service.repository.TagRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl
        implements TagService {

    private final TagRepository tagRepository;

    private final PostTagRepository postTagRepository;

    private final PostRepository postRepository;


    private final PostService postService;


    @Override
    public List<TagResponse> getAllTags() {

        return tagRepository
                .findAll()
                .stream()
                .map(
                        tag ->
                                TagResponse.builder()
                                        .id(tag.getId())
                                        .name(tag.getName())
                                        .build()
                )
                .toList();
    }


    @Override
    public List<PostResponse>
    getPostsByTag(
            String tagName) {

        Tag tag =
                tagRepository
                        .findByNameIgnoreCase(
                                tagName.trim()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Tag not found: "
                                                        + tagName
                                        )
                        );


        List<PostTag> mappings =
                postTagRepository
                        .findByTagId(
                                tag.getId()
                        );


        return mappings
                .stream()
                .map(
                        PostTag::getPostId
                )
                .distinct()
                .map(postService::getById)
                .toList();
    }
}