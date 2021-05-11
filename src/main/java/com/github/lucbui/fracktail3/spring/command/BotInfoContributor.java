package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotInfoContributor implements InfoContributor {
    @Autowired
    private Bot bot;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> botInfo = new HashMap<>();
        botInfo.put("platforms", bot.getPlatforms());
        Map<String, Object> commands = bot.getSpec().getCommandList().getCommands()
                .stream()
                .collect(Collectors.toMap(Command::getId, c -> {
                    Map<String, Object> cmd = new HashMap<>();
                    cmd.put("names", c.getNames());
                    cmd.put("usage", c.getHelp());
                    cmd.put("args", c.getParameters());
                    return cmd;
                }));
        botInfo.put("commands", commands);
        Map<String, Object> scheduled = bot.getSpec().getScheduledEvents().getAll()
                .stream()
                .collect(Collectors.toMap(ScheduledEvent::getId, c -> {
                    Map<String, Object> cmd = new HashMap<>();
                    cmd.put("trigger", c.getTrigger());
                    return cmd;
                }));
        botInfo.put("scheduled", scheduled);
        builder.withDetails(Collections.singletonMap("bot", botInfo));
    }
}
