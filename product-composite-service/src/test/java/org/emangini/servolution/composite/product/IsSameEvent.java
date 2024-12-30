package org.emangini.servolution.composite.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.event.Event;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Slf4j
public class IsSameEvent extends TypeSafeMatcher<String> {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS)
            .disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    // TODO handle raw use
    private Event exectedEvent;

    // TODO handle raw use
    private IsSameEvent(Event exectedEvent) {
        this.exectedEvent = exectedEvent;
    }

    @Override
    protected boolean matchesSafely(String eventAsJson) {

        if (exectedEvent == null) return false;

        log.trace("Convert following json string to a map: {}", eventAsJson);
        // TODO handle raw use
        Map mapEvent = convertJsonStringToMap(eventAsJson);
        mapEvent.remove("eventCreatedAt");

        // TODO handle raw use
        Map mapExpectedEvent = getMapWithoutCreatedAt(exectedEvent);

        log.trace("Got map: {}", mapEvent);
        log.trace("Comparing to expected map: {}", mapExpectedEvent);
        return mapEvent.equals(mapExpectedEvent);

    }

    @Override
    public void describeTo(Description description) {
        String expectedJson = convertObjectToJsonString(exectedEvent);
        description.appendText("is same event as: " + expectedJson);
    }

    /*
       Helper methods
    */
    // TODO handle raw use
    public static Matcher<String> sameEventExceptCreatedAt(Event expectedEvent) {
        return new IsSameEvent(expectedEvent);
    }

    // TODO handle raw use Event, Map
    private Map getMapWithoutCreatedAt(Event event) {
        Map mappedEvent = convertObjectToMap(event);
        mappedEvent.remove("eventCreatedAt");
        return mappedEvent;
    }

    // TODO handle raw use Map
    private Map convertObjectToMap(Object object) {
        JsonNode node = mapper.convertValue(object, JsonNode.class);
        return mapper.convertValue(node, Map.class);
    }

    private String convertObjectToJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO handle raw use Map
    private Map convertJsonStringToMap(String eventAsJson) {
        try {
            return mapper.readValue(eventAsJson, new TypeReference<>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
