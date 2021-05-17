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
public class SnowflakeToObjConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(Snowflake.class, String.class),
                new ConvertiblePair(Snowflake.class, Number.class),
                new ConvertiblePair(Snowflake.class, Instant.class)
        };
        return SetUtils.hashSet(pairs);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Snowflake s = (Snowflake) source;
        if(targetType.getType() == String.class) {
            return s.asString();
        } else if(targetType.getType() == BigInteger.class) {
            return s.asBigInteger();
        } else if(targetType.getType() == Instant.class) {
            return s.getTimestamp();
        } else {
            return s.asLong();
        }
    }
}
