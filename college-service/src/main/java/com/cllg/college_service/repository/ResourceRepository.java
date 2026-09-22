package com.cllg.college_service.repository;

import com.cllg.college_service.entity.Resource;
import com.cllg.college_service.enums.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource,Long> {


    List<Resource>
    findAllByOrderByCreatedAtDesc();


    List<Resource>
    findByDepartmentIdOrderByCreatedAtDesc(
            Long departmentId
    );


    List<Resource>
    findByResourceCategoryOrderByCreatedAtDesc(
            ResourceCategory resourceCategory
    );


    List<Resource>
    findByYearOrderByCreatedAtDesc(
            Integer year
    );

}
