package org.emangini.servolution.api.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

public class CustomZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    private static final DateTimeFormatter dateTimeFormatter = ISO_ZONED_DATE_TIME;

    public CustomZonedDateTimeSerializer() {
        this(null);
    }

    public CustomZonedDateTimeSerializer(final Class<ZonedDateTime> zonedDateTime) {
        super(zonedDateTime);
    }

    @Override
    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeString(dateTimeFormatter.format(zonedDateTime));
    }

}
