package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.object.entity.Member;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class DiscordMemberPerson implements Person {
    private Member member;

    @Override
    public String getName() {
        return member.getDisplayName();
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return member.getPrivateChannel().map(DiscordPlace::new);
    }
}
