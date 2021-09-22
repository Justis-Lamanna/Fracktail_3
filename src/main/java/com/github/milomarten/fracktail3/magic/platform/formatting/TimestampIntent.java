package com.github.milomarten.fracktail3.magic.platform.formatting;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Function;

public class TimestampIntent implements Intent {
    private final Format format;

    public TimestampIntent(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public Formatting getDefaultFormatting() {
        return format.getDefaultFormatting();
    }

    public enum Format {
        SHORT_DATE_TIME(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
        LONG_DATE_TIME(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)),
        SHORT_DATE(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        LONG_DATE(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
        SHORT_TIME(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)),
        LONG_TIME(DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG)),
        RELATIVE(),
        DEFAULT(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG));

        private Formatting defaultFormatting;

        Format(DateTimeFormatter formatter) {
            this.defaultFormatting = Formatting.transforming(new ToFormattedStr(formatter));
        }

        Format() {
            this.defaultFormatting = Formatting.transforming(s -> {
                try {
                    long rawTimestamp = Long.parseLong(s);
                    Instant i = Instant.ofEpochMilli(rawTimestamp);
                    Duration d = Duration.between(Instant.now(), i);
                    return DurationFormatUtils.formatDurationWords(d.toMillis(), true, true);
                } catch (NumberFormatException | DateTimeException ex) {
                    return "<Unknown Duration>";
                }
            });
        }

        public Formatting getDefaultFormatting() {
            return defaultFormatting;
        }
    }

    private static class ToFormattedStr implements Function<String, String> {
        private final DateTimeFormatter formatter;

        private ToFormattedStr(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public String apply(String s) {
            try {
                long rawTimestamp = Long.parseLong(s);
                Instant i = Instant.ofEpochMilli(rawTimestamp);
                return formatter.format(i);
            } catch (NumberFormatException | DateTimeException ex) {
                return "<Unknown Time>";
            }
        }
    }
}
