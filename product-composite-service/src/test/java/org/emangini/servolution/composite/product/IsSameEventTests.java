package org.emangini.servolution.composite.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.event.Event;
import org.junit.jupiter.api.Test;

import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.emangini.servolution.api.event.Event.Type.CREATE;
import static org.emangini.servolution.api.event.Event.Type.DELETE;
import static org.emangini.servolution.composite.product.IsSameEvent.sameEventExceptCreatedAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class IsSameEventTests {

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS)
            .disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    @Test
    void testEventObjectCompare() throws JsonProcessingException {

        /*
            Event 1 & 2 are the same event, but non-concurrent
            Event 3 & 4 are different events
         */
        Event<Integer, Product> event1 = new Event<>(CREATE, 1,
                new Product(1, "name", 1, null));

        Event<Integer, Product> event2 = new Event<>(CREATE, 1,
                new Product(1, "name", 1, null));

        Event<Integer, Product> event3 = new Event<>(DELETE, 1, null);

        Event<Integer, Product> event4 = new Event<>(CREATE, 2,
                new Product(2, "name", 1, null));

        String event1AsJson = mapper.writeValueAsString(event1);

        // validate Event 1 and 2 are the same
        assertThat(event1AsJson, is(sameEventExceptCreatedAt(event2)));

        // validate Event 1 and 3 are different (EventType)
        assertThat(event1AsJson, not(sameEventExceptCreatedAt(event3)));

        // validate Event 1 and 4 are different (same EventType, different key and productId)
        assertThat(event1AsJson, not(sameEventExceptCreatedAt(event4)));
    }
}
