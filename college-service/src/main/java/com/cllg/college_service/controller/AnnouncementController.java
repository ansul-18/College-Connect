package com.cllg.college_service.controller;

import com.cllg.college_service.dto.request.AnnouncementRequest;
import com.cllg.college_service.dto.response.AnnouncementResponse;
import com.cllg.college_service.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/college/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AnnouncementResponse> create(@Valid @RequestBody AnnouncementRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(announcementService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementResponse>> getAll(){
        return ResponseEntity.ok(announcementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(announcementService.getById(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse> update(@PathVariable Long id,@RequestBody AnnouncementRequest request){
        return ResponseEntity.ok(announcementService.update(id,request));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>
    delete(@PathVariable Long id) {announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
