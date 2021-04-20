package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.object.entity.Member;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;

/**
 * A wrapper around a Discord user
 *
 * Note: You can use this as the input to a format string. %s will display the person's name,
 * and %#s will print a ping output ("@user"). All other flags, width, and precision are ignored.
 */
@Data
public class DiscordPerson implements Person, Formattable {
    private final discord4j.core.object.entity.User user;

    @Override
    public String getName() {
        return user instanceof Member ?
                ((Member) user).getDisplayName() :
                user.getUsername();
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return user.getPrivateChannel()
                .map(DiscordPlace::new);
    }

    @Override
    public boolean isBot() {
        return user.isBot();
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? user.getMention() : getName();
        formatter.format(output);
    }
}
