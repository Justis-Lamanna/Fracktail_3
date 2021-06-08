package com.github.milomarten.fracktail3.magic.params.jsr380;

import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;

public enum JSR380DateType {
    DATE(Date.class) {
        @Override
        public int compareToNow(Object input) {
            return ((Date)input).compareTo(new Date());
        }
    },
    CALENDAR(Calendar.class) {
        @Override
        public int compareToNow(Object input) {
            return ((Calendar)input).compareTo(Calendar.getInstance());
        }
    },
    INSTANT(Instant.class) {
        @Override
        public int compareToNow(Object input) {
            return ((Instant)input).compareTo(Instant.now());
        }
    },
    LOCAL_DATE(LocalDate.class) {
        @Override
        public int compareToNow(Object input) {
            return ((LocalDate)input).compareTo(LocalDate.now());
        }
    },
    LOCAL_DATE_TIME(LocalDateTime.class) {
        @Override
        public int compareToNow(Object input) {
            return ((LocalDateTime)input).compareTo(LocalDateTime.now());
        }
    },
    LOCAL_TIME(LocalTime.class) {
        @Override
        public int compareToNow(Object input) {
            return ((LocalTime)input).compareTo(LocalTime.now());
        }
    },
    MONTH_DAY(MonthDay.class) {
        @Override
        public int compareToNow(Object input) {
            return ((MonthDay)input).compareTo(MonthDay.now());
        }
    },
    OFFSET_DATE_TIME(OffsetDateTime.class) {
        @Override
        public int compareToNow(Object input) {
            return ((OffsetDateTime)input).compareTo(OffsetDateTime.now());
        }
    },
    OFFSET_TIME(OffsetTime.class) {
        @Override
        public int compareToNow(Object input) {
            return ((OffsetTime)input).compareTo(OffsetTime.now());
        }
    },
    YEAR(Year.class) {
        @Override
        public int compareToNow(Object input) {
            return ((Year)input).compareTo(Year.now());
        }
    },
    YEAR_MONTH(YearMonth.class) {
        @Override
        public int compareToNow(Object input) {
            return ((YearMonth)input).compareTo(YearMonth.now());
        }
    },
    ZONED_DATE_TIME(ZonedDateTime.class) {
        @Override
        public int compareToNow(Object input) {
            return ((ZonedDateTime)input).compareTo(ZonedDateTime.now());
        }
    },
    HIJRAH_DATE(HijrahDate.class) {
        @Override
        public int compareToNow(Object input) {
            return ((HijrahDate)input).compareTo(HijrahDate.now());
        }
    },
    JAPANESE_DATE(JapaneseDate.class) {
        @Override
        public int compareToNow(Object input) {
            return ((JapaneseDate)input).compareTo(JapaneseDate.now());
        }
    },
    MINGUO_DATE(MinguoDate.class) {
        @Override
        public int compareToNow(Object input) {
            return ((MinguoDate)input).compareTo(MinguoDate.now());
        }
    },
    THAI_BUDDHIST_DATE(ThaiBuddhistDate.class) {
        @Override
        public int compareToNow(Object input) {
            return ((ThaiBuddhistDate)input).compareTo(ThaiBuddhistDate.now());
        }
    };

    final Class<?> clazz;

    JSR380DateType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public abstract int compareToNow(Object input);
}
