package com.example.kevin.fifastatistics.models.notifications;

import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;

/**
 * Response body received when sending a notification request.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class NotificationResponse {

    @JsonIgnore public static final NotificationResponse ERROR_RESPONSE = newErrorResponse();

    @Getter @JsonProperty("multicast_id") private long multicastId;
    @Getter @JsonProperty("canonical_ids") private int canonicalIds;
    @Getter private int success;
    @Getter private int failure;
    private List<Results> results;

    private static NotificationResponse newErrorResponse() {
        NotificationResponse nr = new NotificationResponse();
        nr.failure = 1;
        nr.success = 0;
        return nr;
    }

    @JsonCreator
    private NotificationResponse() {}

    /** Whether or not the notification sent successfully */
    public boolean isSuccessful() {
        return success == 1;
    }

    @JsonIgnore
    public String getMessageId() {
        return results.get(0).getMessageId();
    }

    @JsonIgnore
    public String getError() {
        return results.get(0).getError();
    }

    @Getter
    public static class Results {
        @JsonProperty("message_id") private String messageId;
        private String error;
    }

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }
}
