package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.discord.EmbedResponse;
import com.github.lucbui.fracktail3.discord.context.DiscordPerson;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.discord.util.spring.DiscordRegistryHook;
import com.github.lucbui.fracktail3.spring.command.annotation.*;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Schedule;
import discord4j.common.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UptimeModule {
    private static final String BOT_TIME = "744390997429059595";
    private static final Snowflake ME = Snowflake.of(248612704019808258L);

    private static final Map<String, List<String>> STAT_GROUPS = new HashMap<>();
    private static final List<String> DEFAULT_GROUP = Arrays.asList("process.start.time", "jvm.memory.used", "jvm.threads.live");

    static {
        STAT_GROUPS.put("memory", Arrays.asList("jvm.memory.used", "jvm.memory.committed", "jvm.memory.max"));
        STAT_GROUPS.put("discord", Collections.singletonList("discord.events"));
        STAT_GROUPS.put("twitch", Collections.singletonList("events4j.published"));
        STAT_GROUPS.put("threads", Arrays.asList("jvm.threads.states", "jvm.threads.live", "jvm.threads.peak"));
        STAT_GROUPS.put("gc", Arrays.asList("jvm.gc.memory.allocated", "jvm.gc.memory.promoted", "jvm.gc.live.data.size", "jvm.gc.max.data.size", "jvm.gc.pause"));
        STAT_GROUPS.put("uptime", Collections.singletonList("process.start.time"));
        STAT_GROUPS.put("cpu", Arrays.asList("system.cpu.count", "system.cpu.usage", "process.cpu.usage"));
    }

    private Instant startTime;

    @Autowired
    private MeterRegistry registry;

    @Autowired
    private DiscordRegistryHook hook;

    @PostConstruct
    private void setStartTime() {
        startTime = Instant.now();
    }

    @Command
    @Usage("Retrieve the uptime of this bot")
    public String uptime() {
        Instant origin = Instant.now();
        Duration sinceBotStartup = Duration.between(startTime, origin);
        String botStatus =
                "I have been alive for " + DurationFormatUtils.formatDurationWords(sinceBotStartup.toMillis(), true, false);

        String discordStatus;
        if(hook.getLastConnect() == null){
            // Has never connected
            discordStatus = "On Discord, I have not been connected yet";
        } else {
            // Has connected at least once
            if(hook.getLastDisconnect() == null ||
                    hook.getLastConnect().isAfter(hook.getLastDisconnect())) {
                // Is currently connected
                Duration sinceLastConnect = Duration.between(hook.getLastConnect(), origin);
                discordStatus = "On Discord, I have been connected for " +
                        DurationFormatUtils.formatDurationWords(sinceLastConnect.toMillis(), true, false);
            } else {
                // Is currently disconnected
                Duration sinceLastDisconnect = Duration.between(hook.getLastDisconnect(), hook.getLastConnect());
                discordStatus = "On Discord, I have been disconnected for " +
                        DurationFormatUtils.formatDurationWords(sinceLastDisconnect.toMillis(), true, false);
            }
        }

        return botStatus + ".\n\t- " + discordStatus + ".";
    }

    @Command
    @Usage("Get the stats of this bot")
    public EmbedResponse stats(@InjectPerson(id = "self") DiscordPerson myself, @Parameter(value = 0, optional = true) String keyword) {
        return new EmbedResponse(spec -> {
            spec.setTitle(myself.getName() + " Stats");
            spec.setThumbnail(myself.getUser().getAvatarUrl());
            spec.setTimestamp(Instant.now());
            if(StringUtils.isBlank(keyword)) {
                populateWith(DEFAULT_GROUP, spec);
            } else if(STAT_GROUPS.containsKey(keyword)) {
                populateWith(STAT_GROUPS.get(keyword), spec);
                spec.setDescription("Group: " + StringUtils.capitalize(keyword));
            } else {
                populateWith(Arrays.asList(keyword.split(",")), spec);
                spec.setDescription("Group: " + keyword);
            }
        });
    }

    private void populateWith(List<String> keywords, EmbedCreateSpec spec) {
        Collection<Meter> meters = keywords.stream()
                .flatMap(k -> registry.find(k).meters().stream())
                .limit(25)
                .collect(Collectors.toList());
        for(Meter meter : meters) {
            String shortenedMeterName = StringUtils.substringAfterLast(meter.getId().getName(), ".");
            String title = meter.getId().getTags().stream()
                    .map(Tag::getValue)
                    .map(StringUtils::capitalize)
                    .collect(Collectors.joining(", ", StringUtils.capitalize(shortenedMeterName) + " ", ""));
            for(Measurement m : meter.measure()) {
                String baseUnit = meter.getId().getBaseUnit();
                String value;
                if(StringUtils.equalsIgnoreCase(baseUnit, "seconds")) {
                    ZonedDateTime time = Instant.ofEpochSecond((long)m.getValue()).atZone(ZoneId.systemDefault());
                    value = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(time);
                    baseUnit = null;
                } else {
                    value = String.format("%,.2f", m.getValue());
                }
                spec.addField(title,
                        value + (baseUnit == null ? "" : " " + baseUnit),
                        true);
            }
        }
    }

    @Schedule
    @Cron(hour = "22", dayOfWeek = "SUN-THU", timezone = "America/Chicago")
    public Mono<Void> sleepTimer(@InjectPlatform DiscordPlatform platform) {
        return platform.sendMessage(BOT_TIME, FormatUtils.mentionUser(ME) + ", GO THE HECK TO SLEEP!!")
                .then();
    }

    @Command
    @Usage("Evaluate an arbitrary math expression")
    @Parameters(
            @Parameter(value = 0, name = "expression", optional = true)
    )
    public String math = "The answer is 3.";
}
