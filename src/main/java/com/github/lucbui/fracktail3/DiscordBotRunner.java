package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private Bot bot;

    @Override
    public void run(String... args) throws Exception {
        bot.start().block();
    }

    @PreDestroy
    public void destroy() throws Exception {
        bot.stop().block();
    }
}
