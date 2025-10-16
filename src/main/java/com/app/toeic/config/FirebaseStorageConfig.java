package com.app.toeic.config;

import com.app.toeic.firebase.model.FirebaseBean;
import com.app.toeic.firebase.model.FirebaseConfig;
import com.app.toeic.firebase.repo.FirebaseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirebaseStorageConfig {
    FirebaseRepository firebaseRepository;
//    @Bean
//    Storage getStorageFirebase() throws IOException {
//        var firebaseConfig = firebaseRepository.findAllByStatus(true).stream().findFirst().orElse(FirebaseConfig.builder().build());
//        var jsonContent = firebaseConfig.getFileJson();
//        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
//        return StorageOptions
//                .newBuilder()
//                .setCredentials(credentials)
//                .setProjectId(firebaseConfig.getProjectId())
//                .build()
//                .getService();
//    }

    @Bean
    FirebaseBean getFirebaseConfig() {
        var firebaseConfig = firebaseRepository
                .findAllByStatus(true)
                .stream()
                .findFirst()
                .orElse(FirebaseConfig.builder().build());
        return new FirebaseBean(firebaseConfig);
    }
}
