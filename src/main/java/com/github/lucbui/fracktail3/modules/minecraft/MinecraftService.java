package com.github.lucbui.fracktail3.modules.minecraft;

import com.github.lucbui.fracktail3.spring.command.annotation.Command;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MinecraftService {
    private final RestTemplate template;

    public MinecraftService() {
        template = new RestTemplateBuilder()
                .build();
    }

    public MinecraftServerStatus getStatus(String url, boolean bedrock) {
        String fullUrl = "https://api.mcsrvstat.us/" + (bedrock ? "bedrock/" : "") + "2/" + url;

        return template.getForObject(fullUrl, MinecraftServerStatus.class);
    }

    @Command
    public String minecraft() {
        MinecraftServerStatus s = getStatus("sint-maarten.apexmc.co", false);
        if(s.isOnline()) {
            String[] players = s.getPlayers().getList();
            if(ArrayUtils.isEmpty(players)) {
                return "Looks like the server is online now, but nobody is playing.";
            } else if(players.length == 1) {
                return "Looks like " + players[0] + " is on the server right now.";
            } else if(players.length < 10) {
                return "Looks like " + String.join(", ", players) + " are on the server right now.";
            } else {
                return "Looks like there are " + players.length + " players on the server right now!";
            }
        } else {
            return "Looks like the server is offline now.";
        }
    }
}
