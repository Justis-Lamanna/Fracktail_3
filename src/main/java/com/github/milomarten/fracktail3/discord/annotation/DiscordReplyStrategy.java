package com.github.milomarten.fracktail3.discord.annotation;

import com.github.milomarten.fracktail3.discord.context.DiscordCommandUseContext;
import com.github.milomarten.fracktail3.spring.command.model.MethodComponent;
import com.github.milomarten.fracktail3.spring.command.plugin.MethodComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DiscordReplyStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordReplyStrategy.class);

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        DiscordReply reply = method.getAnnotation(DiscordReply.class);
        base.addTransformer(ctx -> {
            if(ctx instanceof DiscordCommandUseContext) {
                ((DiscordCommandUseContext)ctx).setReplyStyle(reply.value());
            }
        });
        LOGGER.debug("+-Discord Reply style as {}", reply.value());
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        DiscordReply reply = field.getAnnotation(DiscordReply.class);
        base.addTransformer(ctx -> {
            if(ctx instanceof DiscordCommandUseContext) {
                ((DiscordCommandUseContext)ctx).setReplyStyle(reply.value());
            }
        });
        LOGGER.debug("+-Discord Reply style as {}", reply.value());
        return base;
    }
}
