package com.cllg.chat_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public FileStorageService(
            @Value("${chat.file.upload-dir}") String uploadDir) {

        this.uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.uploadPath);

            System.out.println("CHAT UPLOAD DIRECTORY = "
                    + this.uploadPath);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory", e);
        }
    }

    public StoredFile storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException(
                    "File size cannot exceed 10MB");
        }

        String originalName =
                StringUtils.cleanPath(
                        file.getOriginalFilename() == null
                                ? "file"
                                : file.getOriginalFilename());

        String extension = "";

        int dotIndex = originalName.lastIndexOf(".");

        if (dotIndex >= 0) {
            extension =
                    originalName.substring(dotIndex).toLowerCase();
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path targetPath =
                uploadPath.resolve(fileName)
                        .normalize();

        // Security check
        if (!targetPath.startsWith(uploadPath)) {
            throw new RuntimeException("Invalid file path");
        }

        try (InputStream inputStream = file.getInputStream()) {

            Files.copy(
                    inputStream,
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to store file", e);
        }

        return new StoredFile(
                fileName,
                originalName,
                file.getContentType(),
                file.getSize(),
                targetPath.toString()
        );
    }

    public record StoredFile(
            String fileName,
            String originalFileName,
            String contentType,
            long size,
            String path
    ) {}
}