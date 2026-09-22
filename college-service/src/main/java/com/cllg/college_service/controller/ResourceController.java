package com.cllg.college_service.controller;

import com.cllg.college_service.dto.response.ResourceResponse;
import com.cllg.college_service.enums.ResourceCategory;
import com.cllg.college_service.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/college/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;


    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResourceResponse>
    upload(

            @RequestParam String title,

            @RequestParam(required = false)
            String description,

            @RequestParam
            ResourceCategory category,

            @RequestParam(required = false)
            Long departmentId,

            @RequestParam
            Integer year,

            @RequestParam
            Long uploadedBy,

            @RequestParam("file")
            MultipartFile file) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        resourceService.upload(
                                title,
                                description,
                                category,
                                departmentId,
                                year,
                                uploadedBy,
                                file
                        )
                );
    }


    @GetMapping
    public ResponseEntity<List<ResourceResponse>>
    getAll() {

        return ResponseEntity.ok(
                resourceService.getAll()
        );
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<ResourceResponse>>
    getByCategory(

            @PathVariable
            ResourceCategory category) {

        return ResponseEntity.ok(
                resourceService.getByCategory(
                        category
                )
        );
    }


    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ResourceResponse>>
    getByDepartment(

            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                resourceService.getByDepartment(
                        departmentId
                )
        );
    }


    @GetMapping("/year/{year}")
    public ResponseEntity<List<ResourceResponse>>
    getByYear(

            @PathVariable Integer year) {

        return ResponseEntity.ok(
                resourceService.getByYear(
                        year
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(
            @PathVariable Long id) {

        resourceService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
