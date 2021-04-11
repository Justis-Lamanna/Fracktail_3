package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.MultiPlace;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;

/**
 * A wrapper around a Discord Role
 *
 * Allows you to message everyone in a certain role
 * Note: You can use this as the input to a format string. %s will display the channel's name,
 * and %#s will print a mention output ("@role"). All other flags, width, and precision are ignored.
 */
@Data
public class DiscordRolePerson implements Person, Formattable {
    private final Role role;

    @Override
    public String getName() {
        return role.getName();
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return role.getGuild()
                .flatMapMany(Guild::getMembers)
                .filter(m -> m.getRoleIds().contains(role.getId()))
                .map(DiscordMemberPerson::new)
                .flatMap(DiscordMemberPerson::getPrivateChannel)
                .collectList()
                .map(MultiPlace::new);
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? role.getMention() : getName();
        formatter.format(output);
    }
}
