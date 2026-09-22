package com.cllg.community_service.repository;



import com.cllg.community_service.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository
        extends JpaRepository<PostTag, Long> {

    List<PostTag>
    findByPostId(
            Long postId
    );


    List<PostTag>
    findByTagId(
            Long tagId
    );


    void deleteByPostId(
            Long postId
    );


    boolean existsByPostIdAndTagId(
            Long postId,
            Long tagId
    );
}