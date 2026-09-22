package com.cllg.mentor_service.controller;

import com.cllg.mentor_service.dto.request.MentorRequest;
import com.cllg.mentor_service.dto.response.MentorResponse;

import com.cllg.mentor_service.enums.MentorType;

import com.cllg.mentor_service.service.MentorService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorResponse> create(
            @Valid
            @RequestBody
            MentorRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        mentorService.create(
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<MentorResponse>> getAll() {

        return ResponseEntity.ok(
                mentorService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                mentorService.getById(id)
        );
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<MentorResponse>>
    getByDepartment(
            @PathVariable Long departmentId
    ) {

        return ResponseEntity.ok(
                mentorService.getByDepartment(
                        departmentId
                )
        );
    }

    @GetMapping("/type/{mentorType}")
    public ResponseEntity<List<MentorResponse>>
    getByType(
            @PathVariable MentorType mentorType
    ) {

        return ResponseEntity.ok(
                mentorService.getByType(
                        mentorType
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<MentorResponse>>
    search(
            @RequestParam String skill
    ) {

        return ResponseEntity.ok(
                mentorService.search(skill)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorResponse> update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            MentorRequest request
    ) {

        return ResponseEntity.ok(
                mentorService.update(
                        id,
                        request
                )
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        mentorService.deactivate(id);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        mentorService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
    @GetMapping("/{id}/user-id")
    public ResponseEntity<Long> getMentorUserId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                mentorService.getUserId(id)
        );
    }

    @GetMapping("/by-user/{userId}/id")
    public ResponseEntity<Long> getMentorProfileIdByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                mentorService.getMentorProfileIdByUserId(userId)
        );
    }


}