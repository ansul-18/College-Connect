package com.cllg.mentor_service.controller;

import com.cllg.mentor_service.dto.response.MentorAccessResponse;
import com.cllg.mentor_service.service.MentorAccessService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentors/access")
@RequiredArgsConstructor
public class MentorAccessController {

    private final MentorAccessService mentorAccessService;

    @GetMapping
    public ResponseEntity<MentorAccessResponse> getAccess(
            @RequestParam Long mentorId,
            Authentication authentication
    ) {

        Long studentId =
                Long.parseLong(
                        authentication.getName()
                );

        return ResponseEntity.ok(
                mentorAccessService.getAccess(
                        studentId,
                        mentorId
                )
        );
    }

    @GetMapping("/chat")
    public ResponseEntity<Boolean> canChat(
            @RequestParam Long mentorId,
            Authentication authentication
    ) {

        Long studentId =
                Long.parseLong(
                        authentication.getName()
                );

        return ResponseEntity.ok(
                mentorAccessService.canChat(
                        studentId,
                        mentorId
                )
        );
    }
}