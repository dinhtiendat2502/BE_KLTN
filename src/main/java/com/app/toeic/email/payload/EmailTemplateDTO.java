package com.app.toeic.email.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplateDTO {
    private Integer id;

    private String name;
    private String subject;
    private String templateCode;

    private String templateContent;
}
