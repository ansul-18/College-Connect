package com.cllg.college_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService{

    private final Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile file, String folder) {

        validateFile(file);

        try {
            Map<?,?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder",folder,"resource_type",
                    "image","use_filename", true,"unique_filename", true,"overwrite", false));

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new IllegalStateException("Unable to upload image");
        }
    }

    //validateFile
    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
    }

    @Override
    public String uploadRawFile(
            MultipartFile file,
            String folder) {

        validateFile(file);


        try {

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folder, "resource_type",
                                    "raw", "use_filename",true, "unique_filename",true, "overwrite", false));


            return result.get("secure_url").toString();


        } catch (IOException ex) {

            throw new IllegalStateException(
                    "Unable to upload file"
            );
        }
    }
}
