package com.github.lucbui.fracktail3.magic.params.jsr380;

import lombok.experimental.UtilityClass;
import org.springframework.core.convert.TypeDescriptor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.*;

@UtilityClass
public class JSR380TypeDescriptors {
    public static final TypeDescriptor BOOLEAN = TypeDescriptor.valueOf(Boolean.class);

    public static final TypeDescriptor BIG_INTEGER = TypeDescriptor.valueOf(BigInteger.class);
    public static final TypeDescriptor BIG_DECIMAL = TypeDescriptor.valueOf(BigDecimal.class);
    public static final TypeDescriptor BYTE = TypeDescriptor.valueOf(Byte.class);
    public static final TypeDescriptor SHORT = TypeDescriptor.valueOf(Short.class);
    public static final TypeDescriptor INTEGER = TypeDescriptor.valueOf(Integer.class);
    public static final TypeDescriptor LONG = TypeDescriptor.valueOf(Long.class);

    public static final TypeDescriptor DATE = TypeDescriptor.valueOf(Date.class);
    public static final TypeDescriptor CALENDAR = TypeDescriptor.valueOf(Calendar.class);
    public static final TypeDescriptor INSTANT = TypeDescriptor.valueOf(Instant.class);
    public static final TypeDescriptor LOCAL_DATE = TypeDescriptor.valueOf(LocalDate.class);
    public static final TypeDescriptor LOCAL_DATE_TIME = TypeDescriptor.valueOf(LocalDateTime.class);
    public static final TypeDescriptor LOCAL_TIME = TypeDescriptor.valueOf(LocalTime.class);
    public static final TypeDescriptor MONTH_DAY = TypeDescriptor.valueOf(MonthDay.class);
    public static final TypeDescriptor OFFSET_DATE_TIME = TypeDescriptor.valueOf(OffsetDateTime.class);
    public static final TypeDescriptor OFFSET_TIME = TypeDescriptor.valueOf(OffsetTime.class);
    public static final TypeDescriptor YEAR = TypeDescriptor.valueOf(Year.class);
    public static final TypeDescriptor YEAR_MONTH = TypeDescriptor.valueOf(YearMonth.class);
    public static final TypeDescriptor ZONED_DATE_TIME = TypeDescriptor.valueOf(ZonedDateTime.class);
    public static final TypeDescriptor HIJRAH_DATE = TypeDescriptor.valueOf(HijrahDate.class);
    public static final TypeDescriptor JAPANESE_DATE = TypeDescriptor.valueOf(JapaneseDate.class);
    public static final TypeDescriptor MINGUO_DATE = TypeDescriptor.valueOf(MinguoDate.class);
    public static final TypeDescriptor THAI_BUDDHIST_DATE = TypeDescriptor.valueOf(ThaiBuddhistDate.class);

    public static final TypeDescriptor CHAR_SEQUENCE = TypeDescriptor.valueOf(CharSequence.class);
    public static final TypeDescriptor COLLECTION = TypeDescriptor.valueOf(Collection.class);
    public static final TypeDescriptor MAP = TypeDescriptor.valueOf(Map.class);
    public static final TypeDescriptor ARRAY = TypeDescriptor.valueOf(Object[].class);

    public static final TypeDescriptor OBJECT = TypeDescriptor.valueOf(Object.class);

    public static final TypeDescriptor[] NUMBER_TYPES = {
            BIG_DECIMAL, BIG_INTEGER, BYTE, SHORT, INTEGER, LONG
    };

    public static final TypeDescriptor[] DATE_TIME_TYPES = {
            DATE, CALENDAR, INSTANT, LOCAL_DATE, LOCAL_DATE_TIME, LOCAL_TIME, MONTH_DAY, OFFSET_DATE_TIME,
            OFFSET_TIME, YEAR, YEAR_MONTH, ZONED_DATE_TIME, HIJRAH_DATE, JAPANESE_DATE, MINGUO_DATE, THAI_BUDDHIST_DATE
    };

    public static final TypeDescriptor[] NUMBER_TYPES_AND_CHAR_SEQUENCE = {
            BIG_DECIMAL, BIG_INTEGER, BYTE, SHORT, INTEGER, LONG, CHAR_SEQUENCE
    };

    private static final Map<TypeDescriptor, JSR380Types> cache;
    static {
        cache = new HashMap<>();
        cache.put(BOOLEAN, JSR380Types.BOOLEAN);
        cache.put(BYTE, JSR380Types.BYTE);
        cache.put(SHORT, JSR380Types.SHORT);
        cache.put(INTEGER, JSR380Types.INTEGER);
        cache.put(LONG, JSR380Types.LONG);
        cache.put(DATE, JSR380Types.DATE);
        cache.put(CALENDAR, JSR380Types.CALENDAR);
        cache.put(INSTANT, JSR380Types.INSTANT);
        cache.put(LOCAL_DATE, JSR380Types.LOCAL_DATE);
        cache.put(LOCAL_DATE_TIME, JSR380Types.LOCAL_DATE_TIME);
        cache.put(LOCAL_TIME, JSR380Types.LOCAL_TIME);
        cache.put(MONTH_DAY, JSR380Types.MONTH_DAY);
        cache.put(OFFSET_DATE_TIME, JSR380Types.OFFSET_DATE_TIME);
        cache.put(OFFSET_TIME, JSR380Types.OFFSET_TIME);
        cache.put(YEAR, JSR380Types.YEAR);
        cache.put(YEAR_MONTH, JSR380Types.YEAR_MONTH);
        cache.put(ZONED_DATE_TIME, JSR380Types.ZONED_DATE_TIME);
        cache.put(HIJRAH_DATE, JSR380Types.HIJRAH_DATE);
        cache.put(JAPANESE_DATE, JSR380Types.JAPANESE_DATE);
        cache.put(MINGUO_DATE, JSR380Types.MINGUO_DATE);
        cache.put(THAI_BUDDHIST_DATE, JSR380Types.THAI_BUDDHIST_DATE);
        cache.put(CHAR_SEQUENCE, JSR380Types.CHAR_SEQUENCE);
        cache.put(COLLECTION, JSR380Types.COLLECTION);
        cache.put(MAP, JSR380Types.MAP);
        cache.put(ARRAY, JSR380Types.ARRAY);
    }

    public static JSR380Types getJSR380ForType(TypeDescriptor descriptor) {
        return cache.keySet()
                .stream()
                .filter(descriptor::isAssignableTo)
                .map(cache::get)
                .findFirst()
                .orElse(JSR380Types.OBJECT);
    }
}
