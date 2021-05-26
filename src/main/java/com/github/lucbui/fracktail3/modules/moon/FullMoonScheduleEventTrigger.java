package com.github.lucbui.fracktail3.modules.moon;

import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import org.reactivestreams.Publisher;

import java.time.*;

public class FullMoonScheduleEventTrigger implements ScheduleEventTrigger {
    static final ZonedDateTime CALENDAR_REFORM_DAY = LocalDate.of(1582, Month.OCTOBER, 5)
            .atStartOfDay(ZoneId.systemDefault());

    static final double EPOCH                           = 2444238.5;    /* 1980 January 0.0. */
    static final double SUN_ELONG_EPOCH                 = 278.833540;   /* Ecliptic longitude of the Sun at epoch 1980.0. */
    static final double SUN_ELONG_PERIGEE               = 282.596403;   /* Ecliptic longitude of the Sun at perigee. */
    static final double ECCENT_EARTH_ORBIT              = 0.016718;     /* Eccentricity of Earth's orbit. */
    static final double MOON_MEAN_LONGITUDE_EPOCH       = 64.975464;    /* Moon's mean lonigitude at the epoch. */
    static final double MOON_MEAN_LONGITUDE_PERIGREE    = 349.383063;   /* Mean longitude of the perigee at the epoch. */
    static final double KEPLER_EPSILON                  = 1E-6;         /* Accuracy of the Kepler equation. */

    @Override
    public Publisher<Instant> schedule(Scheduler scheduler) {
        return scheduler.cron("0 0 21 * * *")
                .filter(instant -> isFullMoon(instant.atZone(ZoneId.systemDefault())));
    }

    private boolean isFullMoon(ZonedDateTime day) {
        MoonPhase phase = getPhase(toJulian(day));
        return phase.isFull();
    }

    static double toJulian(ZonedDateTime day) {
        int b, m;
        long y;

        m = day.getMonth().getValue();
        y = day.getYear();

        if(day.getMonth().getValue() <= 3) {
            y--;
            m += 12;
        }

        if(day.isBefore(CALENDAR_REFORM_DAY)) {
            b = 0;
        } else {
            int a = (int)(y / 100);
            b = 2 - a + (a / 4);
        }

        return (((long) (365.25 * (y + 4716))) + ((int) (30.6001 * (m + 1))) +
                day.getDayOfMonth() + b - 1524.5) +
                ((day.getSecond() + 60L * (day.getMinute() + 60L * day.getHour())) / 86400.0);
    }

    static MoonPhase getPhase(double jDate) {
        double dateFromEpoch;
        double sunEccent;
        double sunMeanAnomaly;
        double sunPerigreeCoordinatesToEpoch;
        double sunGeocentricElong;
        double moonEvection;
        double moonVariation;
        double moonMeanAnomaly;
        double moonMeanLongitude;
        double moonAnnualEquation;
        double moonCorrectionTerm1;
        double moonCorrectionTerm2;
        double moonCorrectionEquationOfCenter;
        double moonCorrectedAnomaly;
        double moonCorrectedLongitude;
        double moonPresentAge;
        double moonPresentPhase;
        double moonPresentLongitude;

        dateFromEpoch = jDate - EPOCH;

        // Sun Stuff
        sunMeanAnomaly = fixAngle((360.0 / 365.2422) * dateFromEpoch);
        sunPerigreeCoordinatesToEpoch = fixAngle(sunMeanAnomaly + SUN_ELONG_EPOCH - SUN_ELONG_PERIGEE);
        sunEccent = kepler(sunPerigreeCoordinatesToEpoch);
        sunEccent = Math.sqrt((1.0 + ECCENT_EARTH_ORBIT) / (1.0 - ECCENT_EARTH_ORBIT)) * Math.tan(sunEccent / 2.0);
        sunEccent = 2 * Math.toDegrees(Math.atan(sunEccent));
        sunGeocentricElong = fixAngle(sunEccent + SUN_ELONG_PERIGEE);

        // Moon Stuff
        moonMeanLongitude = fixAngle(13.1763966 * dateFromEpoch + MOON_MEAN_LONGITUDE_EPOCH);
        moonMeanAnomaly = fixAngle(moonMeanLongitude - 0.1114041 * dateFromEpoch - MOON_MEAN_LONGITUDE_PERIGREE);
        moonEvection = 1.2739 * Math.sin(Math.toRadians(2.0 * (moonMeanLongitude - sunGeocentricElong) - moonMeanAnomaly));
        moonAnnualEquation = 0.1858 * Math.sin(Math.toRadians(sunPerigreeCoordinatesToEpoch));
        moonCorrectionTerm1 = 0.37 * Math.sin(Math.toRadians(sunPerigreeCoordinatesToEpoch));
        moonCorrectedAnomaly = moonMeanAnomaly + moonEvection - moonAnnualEquation - moonCorrectionTerm1;
        moonCorrectionEquationOfCenter = 6.2886 * Math.sin(Math.toRadians(moonCorrectedAnomaly));
        moonCorrectionTerm2 = 0.214 * Math.sin(Math.toRadians(2.0 * moonCorrectedAnomaly));
        moonCorrectedLongitude = moonMeanLongitude + moonEvection + moonCorrectionEquationOfCenter - moonAnnualEquation + moonCorrectionTerm2;
        moonVariation = 0.6853 * Math.sin(Math.toRadians(2.0 * (moonCorrectedLongitude - sunGeocentricElong)));

        moonPresentLongitude = moonCorrectedLongitude + moonVariation;
        moonPresentAge = moonPresentLongitude - sunGeocentricElong;
        moonPresentPhase = 100.0 * ((1.0 - Math.cos(Math.toRadians(moonPresentAge))) / 2.0);

        if(0.0 < fixAngle(moonPresentAge) - 180.0) {
            moonPresentPhase = -moonPresentPhase;
        }

        return new MoonPhase(moonPresentPhase);
    }

    /*
     * Some useful mathematical functions used by John Walkers `phase()' function.
     */
    public static double fixAngle(double a) {
        return (a) - 360.0 * (Math.floor((a) / 360.0));
    }

    public static double kepler(double m) {
        double e;
        double delta;
        e = m = Math.toRadians(m);
        do {
            delta = e - ECCENT_EARTH_ORBIT * Math.sin(e) - m;
            e -= delta / (1.0 - ECCENT_EARTH_ORBIT * Math.cos(e));
        } while (Math.abs(delta) - KEPLER_EPSILON > 0.0);

        return (e);
    }
}
