package com.cllg.community_service.repository;




import com.cllg.community_service.entity.Post;
import com.cllg.community_service.enums.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository
        extends JpaRepository<Post, Long> {

    List<Post>
    findAllByOrderByCreatedAtDesc();


    List<Post>
    findByAuthorIdOrderByCreatedAtDesc(
            Long authorId
    );


    List<Post>
    findByCategoryOrderByCreatedAtDesc(
            PostCategory category
    );


    List<Post>
    findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            String content
    );
}