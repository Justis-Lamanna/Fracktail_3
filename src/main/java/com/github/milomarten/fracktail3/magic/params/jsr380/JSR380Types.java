package com.github.milomarten.fracktail3.magic.params.jsr380;

import com.github.milomarten.fracktail3.magic.params.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public enum JSR380Types {
    OBJECT,
    BOOLEAN {
        @Override
        public TypeLimits assertFalse() {
            return new IsLimit<>(false);
        }

        @Override
        public TypeLimits assertTrue() {
            return new IsLimit<>(true);
        }
    },
    BYTE {
        @Override
        public TypeLimits max(long value) {
            return RangeLimit.max((byte)value);
        }

        @Override
        public TypeLimits min(long value) {
            return RangeLimit.min((byte)value);
        }

        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(Byte.parseByte(value));
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(Byte.parseByte(value));
        }

        @Override
        public TypeLimits positive(boolean orZero) {
            return orZero ? RangeLimit.min((byte)0) : RangeLimit.min((byte)1);
        }

        @Override
        public TypeLimits negative(boolean orZero) {
            return orZero ? RangeLimit.max((byte)0) : RangeLimit.max((byte)-1);
        }
    },
    SHORT {
        @Override
        public TypeLimits max(long value) {
            return RangeLimit.max((short)value);
        }

        @Override
        public TypeLimits min(long value) {
            return RangeLimit.min((short)value);
        }

        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(Short.parseShort(value));
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(Short.parseShort(value));
        }

        @Override
        public TypeLimits positive(boolean orZero) {
            return orZero ? RangeLimit.min((short)0) : RangeLimit.min((short)1);
        }

        @Override
        public TypeLimits negative(boolean orZero) {
            return orZero ? RangeLimit.max((short)0) : RangeLimit.max((short)-1);
        }
    },
    INTEGER {
        @Override
        public TypeLimits max(long value) {
            return RangeLimit.max((int)value);
        }

        @Override
        public TypeLimits min(long value) {
            return RangeLimit.min((int)value);
        }

        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(Integer.parseInt(value));
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(Integer.parseInt(value));
        }

        @Override
        public TypeLimits positive(boolean orZero) {
            return orZero ? RangeLimit.min(0) : RangeLimit.min(1);
        }

        @Override
        public TypeLimits negative(boolean orZero) {
            return orZero ? RangeLimit.max(0) : RangeLimit.max(-1);
        }
    },
    LONG {
        @Override
        public TypeLimits max(long value) {
            return RangeLimit.max(value);
        }

        @Override
        public TypeLimits min(long value) {
            return RangeLimit.min(value);
        }

        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(Long.parseLong(value));
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(Long.parseLong(value));
        }

        @Override
        public TypeLimits positive(boolean orZero) {
            return orZero ? RangeLimit.min(0L) : RangeLimit.min(1L);
        }

        @Override
        public TypeLimits negative(boolean orZero) {
            return orZero ? RangeLimit.max(0L) : RangeLimit.max(-1L);
        }
    },
    BIG_INTEGER {
        @Override
        public TypeLimits max(long value) {
            return RangeLimit.max(BigInteger.valueOf(value));
        }

        @Override
        public TypeLimits min(long value) {
            return RangeLimit.min(BigInteger.valueOf(value));
        }

        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(new BigInteger(value));
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(new BigInteger(value));
        }

        @Override
        public TypeLimits positive(boolean orZero) {
            return orZero ? RangeLimit.min(BigInteger.ZERO) : RangeLimit.min(BigInteger.ONE);
        }

        @Override
        public TypeLimits negative(boolean orZero) {
            return orZero ? RangeLimit.max(BigInteger.ZERO) : RangeLimit.max(BigInteger.ONE.negate());
        }
    },
    BIG_DECIMAL {
        @Override
        public TypeLimits max(long value) {
            return RangeLimit.max(BigDecimal.valueOf(value));
        }

        @Override
        public TypeLimits min(long value) {
            return RangeLimit.min(BigDecimal.valueOf(value));
        }

        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(new BigDecimal(value));
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(new BigDecimal(value));
        }

        @Override
        public TypeLimits positive(boolean orZero) {
            return orZero ? RangeLimit.min(BigDecimal.ZERO) : RangeLimit.min(BigDecimal.ONE);
        }

        @Override
        public TypeLimits negative(boolean orZero) {
            return orZero ? RangeLimit.max(BigDecimal.ZERO) : RangeLimit.max(BigDecimal.ONE.negate());
        }
    },
    CHAR_SEQUENCE {
        @Override
        public TypeLimits decimalMax(String value) {
            return RangeLimit.max(value);
        }

        @Override
        public TypeLimits decimalMin(String value) {
            return RangeLimit.min(value);
        }

        @Override
        public TypeLimits email() {
            return new EmailLimit();
        }

        @Override
        public TypeLimits notBlank() {
            return new NotBlankLimit();
        }

        @Override
        public TypeLimits pattern(String regex) {
            return new RegexLimit(regex);
        }

        @Override
        public TypeLimits notEmpty() {
            return StringLengthLimit.atLeast(1);
        }

        @Override
        public TypeLimits size(int min, int max) {
            return StringLengthLimit.between(min, max);
        }
    },
    COLLECTION {
        @Override
        public TypeLimits notEmpty() {
            return CollectionLengthLimit.atLeast(1);
        }

        @Override
        public TypeLimits size(int min, int max) {
            return CollectionLengthLimit.between(min, max);
        }
    },
    MAP {
        @Override
        public TypeLimits notEmpty() {
            return MapLengthLimit.atLeast(1);
        }

        @Override
        public TypeLimits size(int min, int max) {
            return MapLengthLimit.between(min, max);
        }
    },
    ARRAY {
        @Override
        public TypeLimits notEmpty() {
            return ArrayLengthLimit.atLeast(1);
        }

        @Override
        public TypeLimits size(int min, int max) {
            return ArrayLengthLimit.between(min, max);
        }
    },
    DATE {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.DATE, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.DATE, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    CALENDAR {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.CALENDAR, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.CALENDAR, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    INSTANT {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.INSTANT, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.INSTANT, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    LOCAL_DATE {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.LOCAL_DATE, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.LOCAL_DATE, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    LOCAL_DATE_TIME {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.LOCAL_DATE_TIME, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.LOCAL_DATE_TIME, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    LOCAL_TIME {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.LOCAL_TIME, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.LOCAL_TIME, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    MONTH_DAY {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.MONTH_DAY, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.MONTH_DAY, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    OFFSET_DATE_TIME {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.OFFSET_DATE_TIME, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.OFFSET_DATE_TIME, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    OFFSET_TIME {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.OFFSET_TIME, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.OFFSET_TIME, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    YEAR {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.YEAR, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.YEAR, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    YEAR_MONTH {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.YEAR_MONTH, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.YEAR_MONTH, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    ZONED_DATE_TIME {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.ZONED_DATE_TIME, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.ZONED_DATE_TIME, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    HIJRAH_DATE {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.HIJRAH_DATE, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.HIJRAH_DATE, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    JAPANESE_DATE {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.JAPANESE_DATE, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.JAPANESE_DATE, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    MINGUO_DATE {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.MINGUO_DATE, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.MINGUO_DATE, DateLimit.Relative.FUTURE, orPresent);
        }
    },
    THAI_BUDDHIST_DATE {
        @Override
        public TypeLimits past(boolean orPresent) {
            return new DateLimit(JSR380DateType.THAI_BUDDHIST_DATE, DateLimit.Relative.PAST, orPresent);
        }

        @Override
        public TypeLimits future(boolean orPresent) {
            return new DateLimit(JSR380DateType.THAI_BUDDHIST_DATE, DateLimit.Relative.FUTURE, orPresent);
        }
    }
    ;

    public TypeLimits notNull() {
        return new IsNotLimit<>(null);
    }

    public TypeLimits isNull() {
        return new IsLimit<>(null);
    }

    public TypeLimits assertFalse() {
        throw new IllegalArgumentException("@AssertFalse does not support " + this.name());
    }

    public TypeLimits assertTrue(){
        throw new IllegalArgumentException("@AssertTrue does not support " + this.name());
    }

    public TypeLimits max(long value) {
        throw new IllegalArgumentException("@Max does not support " + this.name());
    }

    public TypeLimits min(long value) {
        throw new IllegalArgumentException("@Min does not support " + this.name());
    }

    public TypeLimits decimalMax(String value) {
        throw new IllegalArgumentException("@DecimalMax does not support " + this.name());
    }

    public TypeLimits decimalMin(String value) {
        throw new IllegalArgumentException("@DecimalMin does not support " + this.name());
    }

    public TypeLimits positive(boolean orZero) {
        String annot = orZero ? "@PositiveOrZero" : "@Positive";
        throw new IllegalArgumentException(annot + " does not support " + this.name());
    }

    public TypeLimits negative(boolean orZero) {
        String annot = orZero ? "@NegativeOrZero" : "@Negative";
        throw new IllegalArgumentException(annot + " does not support " + this.name());
    }

    public TypeLimits digits(int integer, int fraction) {
        throw new IllegalArgumentException("@Digits does not support " + this.name());
    }

    public TypeLimits email() {
        throw new IllegalArgumentException("@Email does not support " + this.name());
    }

    public TypeLimits notBlank() {
        throw new IllegalArgumentException("@NotBlank does not support " + this.name());
    }

    public TypeLimits pattern(String regex) {
        throw new IllegalArgumentException("@Pattern does not support " + this.name());
    }

    public TypeLimits notEmpty() {
        throw new IllegalArgumentException("@NotEmpty does not support " + this.name());
    }

    public TypeLimits size(int min, int max) {
        throw new IllegalArgumentException("@Size does not support " + this.name());
    }

    public TypeLimits past(boolean orPresent) {
        String annot = orPresent ? "@PastOrPresent" : "@Past";
        throw new IllegalArgumentException(annot + " does not support " + this.name());
    }

    public TypeLimits future(boolean orPresent) {
        String annot = orPresent ? "@FutureOrPresent" : "@Future";
        throw new IllegalArgumentException(annot + " does not support " + this.name());
    }
}
