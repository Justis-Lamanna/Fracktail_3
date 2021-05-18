package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.BasicContextConstructor;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.ParameterParser;
import lombok.Getter;

@Getter
public class DiscordContextConstructor extends BasicContextConstructor {
    private final ReplyStyle replyStyle;

    public DiscordContextConstructor(ParameterParser parameterParser, ReplyStyle replyStyle) {
        super(parameterParser);
        this.replyStyle = replyStyle;
    }

    @Override
    public CommandUseContext constructContext(Bot bot, Platform platform, Message message, Command command, String parameters) {
        return new DiscordCommandUseContext(bot, (DiscordPlatform) platform, message, command,
                getParameterParser().parseParametersFromMessage(command, parameters), replyStyle);
    }
}
