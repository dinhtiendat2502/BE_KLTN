package com.app.toeic.email.model;


import com.app.toeic.util.Constant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "email_template", indexes = {
        @Index(name = "email_template_code_index", columnList = "templateCode"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String name;

    String subject;

    @Column(unique = true)
    String templateCode;

    @Column(columnDefinition = "TEXT")
    String templateContent;
    @Builder.Default
    String status = Constant.STATUS_INACTIVE;
}
