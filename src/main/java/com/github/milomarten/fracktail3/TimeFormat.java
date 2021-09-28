package com.github.milomarten.fracktail3;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

public class TimeFormat {
    public static final ZoneId DEFAULT = ZoneId.of("America/Chicago");

    public static final TimeFormat[] FORMATS = {
            new TimeFormat(LocalDate::parse, ld -> ld.atStartOfDay(DEFAULT)),
            new TimeFormat(LocalTime::parse, lt -> lt.atDate(LocalDate.now(DEFAULT)).atZone(DEFAULT)),
            new TimeFormat(LocalDateTime::parse, ldt -> ldt.atZone(DEFAULT)),
            new TimeFormat(s -> ZonedDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME), Function.identity()),
            new TimeFormat(Duration::parse, d -> ZonedDateTime.now().plus(d))
    };

    private final Function<String, ZonedDateTime> parser;

    public <T> TimeFormat(Function<String, T> parser, Function<T, ZonedDateTime> resolver) {
        this.parser = parser.andThen(resolver);
    }

    public ZonedDateTime parse(String value) {
        return parser.apply(value);
    }

    public static Optional<ZonedDateTime> parseBest(String value, TimeFormat... formats) {
        for(TimeFormat format : formats) {
            try {
                return Optional.of(format.parse(value));
            } catch (DateTimeParseException ignored) { }
        }
        return Optional.empty();
    }
}
