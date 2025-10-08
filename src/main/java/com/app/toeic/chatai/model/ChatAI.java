package com.app.toeic.chatai.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chatai")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatAI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer chatAiId;

    public String projectId;
    public String location;
    public String modelName;
    @Column(length = 5000)
    public String token;
    public String type;
    @Column(length = 5000)
    public String prompt;

    @Builder.Default
    public boolean status = false;
}
