package com.github.lucbui.fracktail3.discord.util.spring;

import discord4j.common.util.Snowflake;
import org.apache.commons.collections4.SetUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Set;

@Component
public class ObjToSnowflakeConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(String.class, Snowflake.class),
                new ConvertiblePair(Number.class, Snowflake.class),
                new ConvertiblePair(Instant.class, Snowflake.class)
        };
        return SetUtils.hashSet(pairs);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType() == String.class) {
            return Snowflake.of((String)source);
        } else if(sourceType.getType() == BigInteger.class) {
            return Snowflake.of((BigInteger)source);
        } else if(sourceType.getType() == Instant.class) {
            return Snowflake.of((Instant) source);
        } else {
            Number n = (Number) source;
            return Snowflake.of(n.longValue());
        }
    }
}
