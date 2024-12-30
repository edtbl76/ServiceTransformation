package org.emangini.servolution.api.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.now;

public class Event<K,T> {

    public enum Type {
        CREATE,
        DELETE
    }


    @Getter
    private final Type eventType;
    @Getter
    private final K key;
    @Getter
    private final T data;

    private final ZonedDateTime eventCreatedAt;

    public Event() {
        this.eventType = null;
        this.key = null;
        this.data = null;
        this.eventCreatedAt = null;
    }

    public Event(Type eventType, K key, T data) {
        this.eventType = eventType;
        this.key = key;
        this.data = data;
        this.eventCreatedAt = now();
    }

    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    public ZonedDateTime getEventCreatedAt() {
        return eventCreatedAt;
    }
}
