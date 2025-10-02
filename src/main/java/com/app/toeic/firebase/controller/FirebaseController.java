package com.app.toeic.firebase.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.model.FirebaseConfig;
import com.app.toeic.firebase.repo.FirebaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/firebase")
@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FirebaseController {
    FirebaseRepository firebaseRepository;

    @GetMapping("/config/all")
    public Object getAll() {
        return firebaseRepository.findAll();
    }

    @PostMapping("/config/add")
    @Transactional
    public Object add(
            @RequestParam("tokenKey") String tokenKey,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("projectId") String projectId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        // check if file ContentType is not application/json
        if (!"application/json".equals(file.getContentType())) {
            return ResponseVO
                    .builder()
                    .success(false)
                    .message("CONTENT_TYPE_NOT_JSON")
                    .data(null)
                    .build();
        }

        String fileContent = new String(file.getBytes());
        var firebaseConfig = FirebaseConfig
                .builder()
                .tokenKey(tokenKey)
                .bucketName(bucketName)
                .projectId(projectId)
                .fileJson(fileContent)
                .build();
        firebaseRepository.save(firebaseConfig);
        return ResponseVO
                .builder()
                .success(true)
                .message("ADD_CONFIG_FIREBASE_SUCCESS")
                .data(null)
                .build();
    }

    @DeleteMapping("/config/remove/{id}")
    public Object remove(@PathVariable Integer id) {
        firebaseRepository.deleteById(id);
        return ResponseVO
                .builder()
                .success(true)
                .message("REMOVE_CONFIG_FIREBASE_SUCCESS")
                .data(null)
                .build();
    }

    @GetMapping("/config/update/{id}")
    public Object update(@PathVariable("id") Integer id) {
        firebaseRepository.findById(id).ifPresent(firebaseConfig -> {
            firebaseConfig.setStatus("ACTIVE");
            firebaseRepository.save(firebaseConfig);
        });
        firebaseRepository.findAllByIdNot(id).forEach(firebaseConfig -> {
            firebaseConfig.setStatus("INACTIVE");
            firebaseRepository.save(firebaseConfig);
        });
        return ResponseVO
                .builder()
                .success(true)
                .message("UPDATE_CONFIG_FIREBASE_SUCCESS")
                .data(null)
                .build();
    }

}
