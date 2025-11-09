package com.app.toeic.part.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PartEnum {
    PART1("PART1", 1),
    PART2("PART2", 2),
    PART3("PART3", 3),
    PART4("PART4", 4),
    PART5("PART5", 5),
    PART6("PART6", 6),
    PART7("PART7", 7);

    String name;
    int value;
}
