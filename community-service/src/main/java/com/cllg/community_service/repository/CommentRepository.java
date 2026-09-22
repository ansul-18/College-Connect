package com.cllg.community_service.repository;



import com.cllg.community_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    List<Comment>
    findByPostIdOrderByCreatedAtAsc(
            Long postId
    );


    long countByPostId(
            Long postId
    );


    List<Comment>
    findByAuthorIdOrderByCreatedAtDesc(
            Long authorId
    );
}