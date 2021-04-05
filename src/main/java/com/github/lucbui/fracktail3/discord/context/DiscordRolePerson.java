package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.MultiPlace;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class DiscordRolePerson implements Person {
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
}
