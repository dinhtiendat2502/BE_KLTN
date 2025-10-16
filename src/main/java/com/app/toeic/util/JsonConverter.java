package com.app.toeic.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.logging.Level;

@Log
@UtilityClass
public class JsonConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String convertObjectToJson(Object param) {
        try {
            return MAPPER.writeValueAsString(param);
        } catch (Exception e) {
            log.log(Level.WARNING, "b2b-service >> JsonConverter >> convertObjectToJson >> Exception:", e);
            return "{}";
        }
    }

    public String convertListToJson(Object param) {
        try {
            return MAPPER.writeValueAsString(param);
        } catch (Exception e) {
            log.log(Level.WARNING, "b2b-service >> JsonConverter >> convertListToJson >> Exception:", e);
            return "[]";
        }
    }

    public static <T> Optional<T> convertToObject(String json, Class<T> typeKey) {
        try {
            var result = MAPPER.readValue(json, typeKey);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.log(Level.WARNING, MessageFormat.format("b2b-service >> JsonConverter >> convertToObject >> request: {0} >> Exception:", json), e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> convertToObject(String json, TypeReference<T> typeKey) {
        try {
            var result = MAPPER.readValue(json, typeKey);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.log(Level.WARNING, MessageFormat.format("b2b-service >> JsonConverter >> convertToObject >> request: {0} >> Exception:", json), e);
            return Optional.empty();
        }
    }

    public static String getPropertyFromJson(String json, String property) {
        try {
            return MAPPER.readTree(json).path(property).asText();
        } catch (Exception e) {
            log.log(Level.WARNING, MessageFormat.format("b2b-service >> JsonConverter >> getPropertyFromJson >> request: {0} >> Exception:", json), e);
            return StringUtils.EMPTY;
        }
    }
}
