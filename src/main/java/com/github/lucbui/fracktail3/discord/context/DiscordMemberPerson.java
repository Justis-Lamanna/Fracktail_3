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
 * A wrapper around a Discord Member.
 * This differs from DiscordPerson in that it includes member data, such as nickname.
 *
 * Note: You can use this as the input to a format string. %s will display the person's name,
 * and %#s will print a ping output ("@user"). All other flags, width, and precision are ignored.
 */
@Data
public class DiscordMemberPerson implements Person, Formattable {
    private final Member member;

    @Override
    public String getName() {
        return member.getDisplayName();
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return member.getPrivateChannel().map(DiscordPlace::new);
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? member.getMention() : getName();
        formatter.format(output);
    }
}
