package com.app.toeic.firebase.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.model.FirebaseConfig;
import com.app.toeic.firebase.repo.FirebaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.text.MessageFormat;
import java.util.logging.Level;

@RestController
@RequestMapping("/firebase/config")
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
            var stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonString = stringBuilder.toString();

            var parser = new JSONParser();
            var jsonObject = (JSONObject) parser.parse(jsonString);

            String projectId = (String) jsonObject.get("project_id");
            var isExist = firebaseRepository
                    .findByProjectId(projectId);
            if (isExist.isPresent()) {
                msg[0] = "ADD_CONFIG_FIREBASE_EXIST";
            } else {
                var firebaseConfig = FirebaseConfig
                        .builder()
                        .tokenKey(tokenKey)
                        .bucketName(MessageFormat.format("{0}.appspot.com", projectId))
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

    @PatchMapping("/update/{id}")
    public Object update(
            @PathVariable("id") Integer id,
            @RequestParam("tokenKey") String tokenKey
    ) throws IOException {
        final String[] msg = new String[1];
        msg[0] = "UPDATE_CONFIG_FIREBASE_SUCCESS";
        var firebaseConfig = firebaseRepository.findById(id);
        if (firebaseConfig.isPresent()) {
            var config = firebaseConfig.get();
            config.setTokenKey(StringUtils.defaultIfBlank(tokenKey, config.getTokenKey()));
            firebaseRepository.save(config);
        } else {
            msg[0] = "UPDATE_CONFIG_FIREBASE_NOT_FOUND";
        }
        return ResponseVO
                .builder()
                .success(true)
                .message(msg[0])
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
