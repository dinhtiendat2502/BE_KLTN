package com.app.toeic.firebase.controller;

import com.app.toeic.firebase.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FirebaseController {
    FirebaseStorageService firebaseStorageService;

    @GetMapping("/test")
    public Object test() {
        return firebaseStorageService.getAllFiles();
    }
}
