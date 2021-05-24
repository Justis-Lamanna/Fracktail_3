package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.platform.Person;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Set;

@Data
public abstract class PlatformSpecificRoleService<T extends Person> {
    private final Class<T> clazz;

    Mono<Set<String>> getRolesForGenericPerson(Person person) {
        return getRolesForPerson((T)person);
    }

    public abstract Mono<Set<String>> getRolesForPerson(T person);

    Mono<Boolean> hasRoleForGenericPerson(Person person, String role) {
        return hasRole((T)person, role);
    }

    public Mono<Boolean> hasRole(T person, String role) {
        return getRolesForPerson(person).map(set -> set.contains(role));
    }
}
