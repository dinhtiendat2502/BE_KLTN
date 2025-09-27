package com.app.toeic.topic.payload;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TopicDto implements Serializable {
    Integer topicId;
    String topicName;
    String topicImage;
}
