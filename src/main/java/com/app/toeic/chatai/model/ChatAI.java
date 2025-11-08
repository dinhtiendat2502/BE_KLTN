package com.app.toeic.chatai.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "chatai", indexes = {
        @Index(name = "model_name_index", columnList = "modelName"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatAI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer chatAiId;

    String projectId;
    String location;
    String modelName;

    @Column(length = 5000)
    String token;

    String type;

    String url;

    @Column(length = 5000)
    @Builder.Default
    String prompt = "%s";

    @Builder.Default
    boolean status = false;
}
