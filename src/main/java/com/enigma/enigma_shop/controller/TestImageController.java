package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.entity.Image;
import com.enigma.enigma_shop.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestImageController {
    private final ImageService imageService;
//    @PostMapping(path = "/api/test-upload")
//    public ResponseEntity<?> testUpload(@RequestPart(name = "image")MultipartFile multipartFile) {
//        Image image = imageService.create(multipartFile);
//        return ResponseEntity.status(HttpStatus.CREATED).body(image);
//    }

    @GetMapping(path = APIUrl.PRODUCT_IMAGE_DOWNLOAD_API + "{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable(name = "imageId") String id) {
        Resource resource = imageService.getById(id);
        String headerValue = String.format("attachment;filename=%s",resource.getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }

}
