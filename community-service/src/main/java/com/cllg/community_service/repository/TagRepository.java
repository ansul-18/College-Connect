package com.cllg.community_service.repository;



import com.cllg.community_service.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository
        extends JpaRepository<Tag, Long> {

    Optional<Tag>
    findByNameIgnoreCase(
            String name
    );


    boolean existsByNameIgnoreCase(
            String name
    );
}