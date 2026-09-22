package com.cllg.college_service.controller;


import com.cllg.college_service.dto.request.ComplaintRequest;
import com.cllg.college_service.dto.response.ComplaintResponse;
import com.cllg.college_service.enums.ComplaintStatus;
import com.cllg.college_service.service.ComplaintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/college/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    private final ObjectMapper objectMapper;


    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ComplaintResponse>
    create(

            @RequestPart("data")
            String data,

            @RequestPart(
                    value = "image",
                    required = false
            )
            MultipartFile image)
            throws Exception {

        ComplaintRequest request =
                objectMapper.readValue(
                        data,
                        ComplaintRequest.class
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        complaintService.create(
                                request,
                                image
                        )
                );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse>
    getById(

            @PathVariable Long id) {

        return ResponseEntity.ok(
                complaintService.getById(id)
        );
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ComplaintResponse>>
    getMyComplaints(

            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                complaintService
                        .getMyComplaints(
                                studentId
                        )
        );
    }


    @GetMapping
    public ResponseEntity<List<ComplaintResponse>>
    getAll() {

        return ResponseEntity.ok(
                complaintService.getAll()
        );
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<ComplaintResponse>>
    getByStatus(

            @PathVariable ComplaintStatus status) {

        return ResponseEntity.ok(
                complaintService.getByStatus(
                        status
                )
        );
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse>
    updateStatus(

            @PathVariable Long id,

            @RequestParam
            ComplaintStatus status) {

        return ResponseEntity.ok(
                complaintService.updateStatus(
                        id,
                        status
                )
        );
    }
}