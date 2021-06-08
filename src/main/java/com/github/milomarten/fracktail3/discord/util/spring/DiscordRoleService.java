package com.github.milomarten.fracktail3.discord.util.spring;

import com.github.milomarten.fracktail3.discord.context.DiscordPerson;
import com.github.milomarten.fracktail3.spring.service.PlatformSpecificRoleService;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * Role Service that services DiscordPersons
 * If the DiscordPerson is a Member, their roles are retrieved in the format role:[role snowflake].
 * The guild is also retrieved, in the format guild:[guild snowflake].
 * In all cases, the user's snowflake is returned as a role, as well as if they are or are not a bot.
 */
@Service
public class DiscordRoleService extends PlatformSpecificRoleService<DiscordPerson> {
    public DiscordRoleService() {
        super(DiscordPerson.class);
    }

    @Override
    public Mono<Set<String>> getRolesForPerson(DiscordPerson person) {
        User user = person.getUser();
        Set<String> roles = new HashSet<>();
        if(user instanceof Member) {
            Member member = (Member) user;
            member.getRoleIds().forEach(snowflake -> roles.add("role:" + snowflake.asString()));
            roles.add("guild:" + member.getGuildId().asString());
        }
        roles.add(user.getId().asString());
        roles.add(user.isBot() ? "bot" : "not-bot");
        return Mono.just(roles)
                .flatMap(set ->
                        person.isOwner().filter(b -> b).thenReturn(set).doOnNext(s -> s.add("owner"))
                );
    }
}
