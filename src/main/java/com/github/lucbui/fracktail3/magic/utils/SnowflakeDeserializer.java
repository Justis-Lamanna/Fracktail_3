package com.github.lucbui.fracktail3.magic.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import discord4j.core.object.util.Snowflake;

import java.io.IOException;

public class SnowflakeDeserializer extends StdDeserializer<Snowflake> {
    public SnowflakeDeserializer() {
        this(null);
    }

    public SnowflakeDeserializer(Class<Snowflake> t) {
        super(t);
    }

    @Override
    public Snowflake deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long literal = node.get("snowflake").asLong();
        return Snowflake.of(literal);
    }
}
