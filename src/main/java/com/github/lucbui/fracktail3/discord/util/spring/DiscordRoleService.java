package com.github.lucbui.fracktail3.discord.util.spring;

import com.github.lucbui.fracktail3.discord.context.DiscordPerson;
import com.github.lucbui.fracktail3.spring.service.PlatformSpecificRoleService;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

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
        return Mono.just(roles);
    }
}
