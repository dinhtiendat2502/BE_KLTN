package com.app.toeic.firebase.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.model.FirebaseConfig;
import com.app.toeic.firebase.repo.FirebaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.logging.Level;

@RestController
@RequestMapping("/firebase/config")
@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Log
public class FirebaseConfigController {
    FirebaseRepository firebaseRepository;

    @GetMapping("/all")
    public Object getAll() {
        return firebaseRepository.findAll();
    }

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    @Transactional
    public Object add(
            @RequestParam("tokenKey") String tokenKey,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        // check if file ContentType is not application/json or file is empty
        if (!"application/json".equals(file.getContentType())
                || file.isEmpty()) {
            return ResponseVO
                    .builder()
                    .success(false)
                    .message("FILE_NOT_VALID")
                    .data(null)
                    .build();
        }
        final String[] msg = new String[1];
        msg[0] = "ADD_CONFIG_FIREBASE_SUCCESS";
        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonString = stringBuilder.toString();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

            String projectId = (String) jsonObject.get("project_id");
            var isExist = firebaseRepository
                    .findByProjectId(projectId);
            if (isExist.isPresent()) {
                msg[0] = "ADD_CONFIG_FIREBASE_EXIST";
            } else {
                FirebaseConfig firebaseConfig = FirebaseConfig
                        .builder()
                        .tokenKey(tokenKey)
                        .bucketName(STR."\{projectId}.appspot.com")
                        .projectId(projectId)
                        .fileJson(jsonString)
                        .build();
                firebaseRepository.save(firebaseConfig);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            msg[0] = "ADD_CONFIG_FIREBASE_FAILED";
        }
        return ResponseVO
                .builder()
                .success(true)
                .message(msg[0])
                .data(null)
                .build();
    }

    @DeleteMapping("/remove/{id}")
    public Object remove(@PathVariable Integer id) {
        firebaseRepository.deleteById(id);
        return ResponseVO
                .builder()
                .success(true)
                .message("REMOVE_CONFIG_FIREBASE_SUCCESS")
                .data(null)
                .build();
    }

    @PatchMapping("/update/status/{id}")
    public Object update(@PathVariable("id") Integer id) {
        firebaseRepository.findById(id).ifPresent(firebaseConfig -> {
            firebaseConfig.setStatus(true);
            firebaseRepository.save(firebaseConfig);
        });
        firebaseRepository.findAllByIdNot(id).forEach(firebaseConfig -> {
            firebaseConfig.setStatus(false);
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
