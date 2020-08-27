package com.github.lucbui.fracktail3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.utils.BotModule;
import discord4j.core.object.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private Bot bot;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new Jdk8Module());
        m.registerModule(new BotModule());
        String json = "{\"snowflake\" : 112005555178000384}";
        Snowflake s = m.readValue(json, Snowflake.class);
        bot.start().block();
    }
}
