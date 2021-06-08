package com.github.milomarten.fracktail3.modules.minecraft;

import com.github.milomarten.fracktail3.discord.EmbedResponse;
import com.github.milomarten.fracktail3.spring.command.annotation.Command;
import com.github.milomarten.fracktail3.spring.command.annotation.Parameter;
import com.github.milomarten.fracktail3.spring.command.annotation.Usage;
import discord4j.rest.util.Color;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class MinecraftService {
    private final WebClient template;

    public MinecraftService() {
        template = WebClient.create();
    }

    public Mono<MinecraftServerStatus> getStatus(String url, boolean bedrock) {
        String fullUrl = "https://api.mcsrvstat.us/" + (bedrock ? "bedrock/" : "") + "2/" + url;

        return template.get()
                .uri(fullUrl)
                .exchange()
                .flatMap(cr -> cr.bodyToMono(MinecraftServerStatus.class));
    }

    @Command
    @Usage("Get Minecraft server statistics")
    public EmbedResponse mc(@Parameter(value = 0, optional = true) String url) {
        String trueUrl = StringUtils.defaultString(url, "sint-maarten.apexmc.co");

        MinecraftServerStatus s = getStatus(trueUrl, false).block();

        return new EmbedResponse(spec -> {
            String title;
            String description;
            if(s == null) {
                title = "???";
                description = null;
            }
            else if(s.getMotd() != null && ArrayUtils.isNotEmpty(s.getMotd().getClean())) {
                String[] motds = s.getMotd().getClean();
                title = StringUtils.firstNonEmpty(s.getHostname(), s.getIp());
                description = motds[0];
            } else if(s.getHostname() != null && s.getIp() != null) {
                title = s.getHostname();
                description = s.getIp();
            } else {
                title = StringUtils.firstNonEmpty(s.getHostname(), s.getIp(), "???");
                description = null;
            }
            spec.setTitle(title);
            if (description != null) { spec.setDescription(description); }
            spec.setAuthor("Minecraft Server Status API", "https://api.mcsrvstat.us/", null);
            spec.setColor(Color.of(22, 22, 22));
            if(s != null && s.isOnline()) {
                spec.setThumbnail("https://api.mcsrvstat.us/icon/" + trueUrl);
                spec.addField("Status", "Online", true);
                spec.addField("Version", s.getVersion(), true);
                spec.addField("Max Players", s.getPlayers() == null ? "N/A" : Integer.toString(s.getPlayers().getMax()), true);
                spec.addField("Players (" + getPlayersInfoCount(s.getPlayers()) + ")", getPlayersInfoText(s.getPlayers()), false);

                spec.addField("Plugins (" + getPluginModInfoCount(s.getPlugins()) + ")", getPluginModInfoText(s.getPlugins()), false);
                spec.addField("Mods (" + getPluginModInfoCount(s.getMods()) + ")", getPluginModInfoText(s.getMods()), false);
                spec.setTimestamp(Instant.now());
            } else {
                spec.addField("Status", "Offline", false);
            }
        });
    }

    public String getPluginModInfoText(ModInfo info) {
        if(info != null && ArrayUtils.isNotEmpty(info.getNames())) {
            return StringUtils.abbreviate(String.join(", ", info.getNames()), 400);
        }
        return "None";
    }

    public String getPlayersInfoText(PlayersInfo info) {
        if(info != null && ArrayUtils.isNotEmpty(info.getList())) {
            return StringUtils.abbreviate(String.join(", ", info.getList()), 400);
        }
        return "None";
    }

    public int getPluginModInfoCount(ModInfo info) {
        return info == null ? 0 : ArrayUtils.getLength(info.getNames());
    }

    public int getPlayersInfoCount(PlayersInfo info) {
        return info == null ? 0 : ArrayUtils.getLength(info.getList());
    }
}
