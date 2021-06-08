package com.github.milomarten.fracktail3.spring.command;

import com.github.milomarten.fracktail3.magic.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class BotInfoContributor implements InfoContributor {
    @Autowired
    private Bot bot;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> botInfo = new HashMap<>();
        botInfo.put("platforms", bot.getPlatforms());
        botInfo.put("commands", bot.getSpec().getCommandList().getCommands());
        builder.withDetails(Collections.singletonMap("bot", botInfo));
    }
}
