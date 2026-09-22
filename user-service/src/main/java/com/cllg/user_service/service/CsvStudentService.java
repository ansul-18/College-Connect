package com.cllg.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface CsvStudentService {

    int importStudents(MultipartFile file);
    byte[] exportStudents();
}
