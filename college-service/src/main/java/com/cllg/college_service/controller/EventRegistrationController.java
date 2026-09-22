package com.cllg.college_service.controller;

import com.cllg.college_service.dto.response.RegistrationResponse;
import com.cllg.college_service.service.EventRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/college/events")
@RequiredArgsConstructor
public class EventRegistrationController {

    private final EventRegistrationService registrationService;


    @PostMapping("/{eventId}/register/{studentId}")
    public ResponseEntity<RegistrationResponse>
    register(@PathVariable Long eventId, @PathVariable Long studentId) {

        return ResponseEntity.ok(registrationService.register(eventId, studentId)
        );
    }


    @DeleteMapping("/{eventId}/register/{studentId}")
    public ResponseEntity<Void> cancel(@PathVariable Long eventId, @PathVariable Long studentId) {

        registrationService.cancel(eventId, studentId);

        return ResponseEntity
                .noContent()
                .build();
    }


    @GetMapping("/{eventId}/registrations")
    public ResponseEntity<List<RegistrationResponse>> getByEvent(@PathVariable Long eventId) {

        return ResponseEntity.ok(registrationService
                        .getByEvent(eventId)
        );
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RegistrationResponse>> getByStudent(@PathVariable Long studentId) {

        return ResponseEntity.ok(registrationService
                        .getByStudent(studentId)
        );
    }
}
