package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.platform.Person;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * An extremely basic role service, which provides generic info based on a generic person.
 * Roles include a user's name and their class. Additionally, the bot or not-bot role is assigned
 * based on if person.isBot() is true or false.
 */
public class BasicRoleService implements RoleService {
    @Override
    public Mono<Set<String>> getRolesForPerson(Person person) {
        return Mono.fromSupplier(() -> {
            Set<String> roles = new HashSet<>();
            roles.add(person.getName());
            roles.add(person.getClass().getSimpleName());
            roles.add(person.isBot() ? "bot" : "not-bot");
            return roles;
        });
    }
}
