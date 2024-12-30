package org.emangini.servolution.api.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    private static final DateTimeFormatter dateTimeFormatter = ISO_LOCAL_DATE_TIME;

    public CustomLocalDateTimeSerializer() {
        this(null);
    }

    public CustomLocalDateTimeSerializer(final Class<LocalDateTime> localDateTime) {
        super(localDateTime);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeString(dateTimeFormatter.format(localDateTime));
    }

}
