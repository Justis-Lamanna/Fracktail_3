package com.github.milomarten.fracktail3.modules.moon;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FullMoonScheduleEventTriggerTest {
    @Test
    public void julianDateCalculatesCorrectly() {
        ZonedDateTime day = LocalDate.of(2013, Month.JANUARY, 1)
                .atTime(LocalTime.of(0, 30, 0, 0))
                .atZone(ZoneId.of("UT"));

        double jDay = FullMoonScheduleEventTrigger.toJulian(day);
        assertEquals(2_456_293.5208333335, jDay); //I'm putting my faith in you, Wikipedia.
    }

    @Test
    public void phaseCalculatesCorrectly_FullMoon() {
        ZonedDateTime day = LocalDate.of(2021, Month.MAY, 26)
                .atTime(LocalTime.of(21, 0, 0, 0))
                .atZone(ZoneId.of("GMT-5"));

        double jDay = FullMoonScheduleEventTrigger.toJulian(day);
        MoonPhase phase = FullMoonScheduleEventTrigger.getPhase(jDay);
        assertTrue(phase.isFull());
    }

    @Test
    public void phaseCalculatesCorrectly_NewMoon() {
        ZonedDateTime day = LocalDate.of(2021, Month.MAY, 11)
                .atTime(LocalTime.of(14, 1, 0, 0))
                .atZone(ZoneId.of("GMT-5"));

        double jDay = FullMoonScheduleEventTrigger.toJulian(day);
        MoonPhase phase = FullMoonScheduleEventTrigger.getPhase(jDay);
        assertTrue(phase.isNew());
    }
}