package com.github.milomarten.fracktail3.magic.params.jsr380;

import com.github.milomarten.fracktail3.magic.params.ClassLimit;

import java.time.Instant;

public class DateLimit extends ClassLimit {
    private final JSR380DateType type;
    private final Relative relative;
    private final boolean includeNow;

    public DateLimit(JSR380DateType type, Relative relative, boolean includeNow) {
        super(type.getClazz());
        this.type = type;
        this.relative = relative;
        this.includeNow = includeNow;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && doRelativeMatch(obj);
    }

    @Override
    public Object getDefault() {
        return Instant.now();
    }

    private boolean doRelativeMatch(Object obj) {
        if(relative == Relative.PAST) {
            if(includeNow) {
                return type.compareToNow(obj) <= 0;
            } else {
                return type.compareToNow(obj) < 0;
            }
        } else {
            if(includeNow) {
                return type.compareToNow(obj) >= 0;
            } else {
                return type.compareToNow(obj) > 0;
            }
        }
    }

    enum Relative {
        PAST,
        FUTURE
    }
}
