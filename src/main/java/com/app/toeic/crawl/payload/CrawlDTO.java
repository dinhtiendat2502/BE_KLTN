package com.app.toeic.crawl.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrawlDTO {
    String url;
    String email;
    String providerCode;
    String agent;
}
