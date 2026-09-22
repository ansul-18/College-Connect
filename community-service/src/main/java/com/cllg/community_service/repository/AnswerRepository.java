package com.cllg.community_service.repository;



import com.cllg.community_service.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository
        extends JpaRepository<Answer, Long> {

    List<Answer>
    findByPostIdOrderByCreatedAtAsc(
            Long postId
    );


    long countByPostId(
            Long postId
    );


    List<Answer>
    findByAuthorIdOrderByCreatedAtDesc(
            Long authorId
    );
}