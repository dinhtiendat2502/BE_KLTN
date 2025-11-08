package com.app.toeic.external.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPooled;

import javax.net.ssl.SSLContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class AssetsController {
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping("/test")
    public ResponseVO test(HttpServletRequest request) {
        var address = new HostAndPort("redis-12988.c292.ap-southeast-1-1.ec2.redns.redis-cloud.com", 12988);
        var config = DefaultJedisClientConfig.builder()
                                             .ssl(true)
                                             .user("default") // use your Redis user. More info https://redis.io/docs/latest/operate/oss_and_stack/management/security/acl/
                                             .password("Ls3ZXG94qw9Zcw6fgLjq1iJUgSaPcxsS") // use your Redis password
                                             .build();
        try (var jedis = new JedisPooled(address, config)) {
            jedis.sadd("kira", "test");
        }
        return new ResponseVO(
                Boolean.TRUE,
                null,
                "Get assets successfully"
        );
    }

    @PostMapping(value = "/upload-file", consumes = {"multipart/form-data"})
    public ResponseVO uploadImage(@RequestParam("file") MultipartFile file) throws
            IOException {
        if (file == null) return new ResponseVO(
                Boolean.FALSE,
                "",
                "File is null"
        );

        var image = firebaseStorageService.uploadFile(file);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(image)
                .message("Upload hình ảnh thành công!")
                .build();
    }

    @PostMapping("/upload-files")
    public ResponseVO uploadImages(@RequestParam("files") MultipartFile[] files) throws
            IOException {
        var images = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            images[i] = firebaseStorageService.uploadFile(files[i]);
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(images)
                .message("Upload hình ảnh thành công!")
                .build();
    }

    @GetMapping(value = "test-stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> streamTest() {
        int maxRecords = 10;
        StreamingResponseBody responseBody = response -> {
            for (int i = 1; i <= maxRecords; i++) {
                var st = new Student("Name" + i, i);
                response.write(JsonConverter.convertObjectToJson(st).getBytes());
                response.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_STREAM_JSON)
                             .body(responseBody);
    }
    @GetMapping(value = "test-flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Student> streamJsonObjects() {
        return Flux.fromStream(Stream.generate(() -> new Student("Name", 1))
                                     .limit(1000000));
    }

    @GetMapping("test-no-stream")
    public Object testFlux() {
        // return 900000 Student objects
        var listStudent = new ArrayList<Student>();

        for (long i = 1; i <= 100000; i++) {
            listStudent.add(new Student("Name" + i, i));
        }

        return listStudent;
    }

    @GetMapping("csv")
    public ResponseEntity<StreamingResponseBody> getCsvFile() {
        StreamingResponseBody stream = output -> {
            var writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write("name,rollNo" + "\n");
            for (long i = 1; i <= 10000000; i++) {
                var st = new Student("Name" + i, i);
                writer.write(st.name + "," + st.rollNo + "\n");
                writer.flush();
            }
        };
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv")
                             .contentType(MediaType.TEXT_PLAIN)
                             .body(stream);
    }

    public record Student(String name, long rollNo) {
    }

}
