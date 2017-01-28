package com.example.kevin.fifastatistics.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import lombok.experimental.UtilityClass;

/**
 * Utility class for serializing and deserializing objects from and to JSON.
 */
@UtilityClass
public class SerializationUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Serializes an object to a JSON String.
     * @param value    The object to serialize
     * @return the JSON string
     */
    public static <T> String toJson(T value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes an object to a pretty-printed JSON String
     * @param value    The object to serialize
     * @return the formatted JSON string
     */
    public static <T> String toFormattedJson(T value) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes a JSON String into an object.
     * @param json  The JSON String
     * @param type  The Class to create an instance of
     * @return the object
     */
    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
