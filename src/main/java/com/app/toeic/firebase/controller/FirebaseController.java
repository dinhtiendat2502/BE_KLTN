package com.app.toeic.firebase.controller;


import com.app.toeic.firebase.repo.FirebaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/firebase")
@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FirebaseController {
    FirebaseRepository firebaseRepository;

    @PostMapping("add")
    public Object add(
            @RequestParam("tokenKey") String tokenKey,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("projectId") String projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("status") String status
    ) {
        return null;
    }

}
