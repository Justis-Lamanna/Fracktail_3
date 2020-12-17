package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAfter;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAt;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunEvery;

import java.lang.annotation.Annotation;

public class AnnotationUtilsTestUtils {
    public static Formatter createFormatter(Class<? extends ContextFormatter> clazz, String... params) {
        return new Formatter(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return Formatter.class;
            }

            @Override
            public Class<? extends ContextFormatter> value() {
                return clazz;
            }

            @Override
            public String[] params() {
                return params;
            }
        };
    }

    public static FString createFString(String base, Formatter... formatters) {
        return new FString(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return FString.class;
            }

            @Override
            public String value() {
                return base;
            }

            @Override
            public Formatter[] formatters() {
                return formatters;
            }
        };
    }

    public static Usage createUsage(String base, Formatter... formatters) {
        return new Usage(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return Usage.class;
            }

            @Override
            public String value() {
                return base;
            }

            @Override
            public Formatter[] formatters() {
                return formatters;
            }
        };
    }

    public static Cron createCron(String second, String minute, String hour, String dom, String month, String dow, String timezone) {
        return new Cron() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Cron.class;
            }

            @Override
            public String second() {
                return second;
            }

            @Override
            public String minute() {
                return minute;
            }

            @Override
            public String hour() {
                return hour;
            }

            @Override
            public String dayOfMonth() {
                return dom;
            }

            @Override
            public String month() {
                return month;
            }

            @Override
            public String dayOfWeek() {
                return dow;
            }

            @Override
            public String timezone() {
                return timezone;
            }
        };
    }

    public static RunAfter createRunAfter(String duration) {
        return new RunAfter(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return RunAfter.class;
            }

            @Override
            public String value() {
                return duration;
            }
        };
    }

    public static RunEvery createRunEvery(String duration) {
        return new RunEvery(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return RunEvery.class;
            }

            @Override
            public String value() {
                return duration;
            }
        };
    }

    public static RunAt createRunAt(String time) {
        return new RunAt(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return RunAt.class;
            }

            @Override
            public String value() {
                return time;
            }
        };
    }
}
