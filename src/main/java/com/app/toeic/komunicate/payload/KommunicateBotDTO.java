package com.app.toeic.komunicate.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KommunicateBotDTO {
    private Integer id;
    private String appId;
    private String apiKey;
}
