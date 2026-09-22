package com.cllg.college_service.controller;

import com.cllg.college_service.dto.request.EventRequest;
import com.cllg.college_service.dto.response.EventResponse;
import com.cllg.college_service.enums.EventStatus;
import com.cllg.college_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/college/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<EventResponse> create(@RequestPart("data") String data, @RequestPart(value = "image", required = false) MultipartFile image)throws Exception {
        EventRequest request = objectMapper.readValue(data,EventRequest.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.create(request, image));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {

        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventResponse>> getByStatus(@PathVariable EventStatus status) {

        return ResponseEntity.ok(eventService.getByStatus(status));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @RequestPart("data") String data,
                                                @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        EventRequest request = objectMapper.readValue(data, EventRequest.class);

        return ResponseEntity.ok(eventService.update(id, request, image));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
