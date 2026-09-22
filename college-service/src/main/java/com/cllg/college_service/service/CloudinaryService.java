package com.cllg.college_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    String uploadImage(MultipartFile file,String folder);
    String uploadRawFile(MultipartFile file,String folder);
}
