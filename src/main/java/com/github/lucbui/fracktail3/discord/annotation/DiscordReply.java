package com.github.lucbui.fracktail3.discord.annotation;

import com.github.lucbui.fracktail3.discord.context.ReplyStyle;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(DiscordReplyStrategy.class)
public @interface DiscordReply {
    ReplyStyle value();
}
