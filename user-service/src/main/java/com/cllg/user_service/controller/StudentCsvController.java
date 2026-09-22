package com.cllg.user_service.controller;

import com.cllg.user_service.service.CsvStudentService;
import com.cllg.user_service.service.StudentServiceIml;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users/students/csv")
@RequiredArgsConstructor
public class StudentCsvController {

    private final CsvStudentService csvStudentService;

    @PostMapping(value = "/import",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file){

        int count = csvStudentService.importStudents(file);
        return ResponseEntity.ok(count + " students imported successfully");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudents(){

        byte[] data = csvStudentService.exportStudents();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        headers.setContentDisposition(ContentDisposition.attachment().filename("students.csv").build());


        return ResponseEntity
                .ok()
                .headers(headers)
                .body(data);
    }


}
