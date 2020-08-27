package com.github.lucbui.fracktail3.magic.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import discord4j.core.object.util.Snowflake;

import java.io.IOException;

public class SnowflakeSerializer extends StdSerializer<Snowflake> {
    public SnowflakeSerializer() {
        this(null);
    }

    public SnowflakeSerializer(Class<Snowflake> t) {
        super(t);
    }

    @Override
    public void serialize(Snowflake snowflake, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("snowflake", snowflake.asLong());
        jsonGenerator.writeEndObject();
    }
}
