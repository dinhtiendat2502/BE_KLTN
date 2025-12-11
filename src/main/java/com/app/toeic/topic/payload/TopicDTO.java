package com.app.toeic.topic.payload;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TopicDTO {
    Integer topicId;
    String topicName;
    String topicImage;
}
