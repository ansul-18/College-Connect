package com.cllg.chat_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Paths;

@Configuration
public class FileResourceConfig
        implements WebMvcConfigurer {

    @Value("${chat.file.upload-dir:uploads/chat}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        String location =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize()
                        .toUri()
                        .toString();

        registry.addResourceHandler(
                        "/uploads/chat/**"
                )
                .addResourceLocations(
                        location
                );
    }
}