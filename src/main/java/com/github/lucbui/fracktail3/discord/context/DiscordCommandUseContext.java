package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscordCommandUseContext implements CommandUseContext {
    private final Bot bot;
    private final DiscordPlatform platform;
    private final Message message;
    private final Command command;
    private final Parameters parameters;

    private ReplyStyle replyStyle = ReplyStyle.PLAIN;

    @Override
    public Message getTrigger() {
        return message;
    }

    @Override
    public Person getSender() {
        return message.getSender();
    }

    @Override
    public Mono<Place> getTriggerPlace() {
        return message.getOrigin();
    }

    @Override
    public Mono<Void> respond(String message, File... files) {
        switch (replyStyle) {
            case DM: return getSender().getPrivateChannel().flatMap(place -> place.sendMessage(message, files)).then();
            case REPLY: return getTrigger().reply(message, files).then();
            default: return getTriggerPlace().flatMap(place -> place.sendMessage(message, files)).then();
        }
    }
}
