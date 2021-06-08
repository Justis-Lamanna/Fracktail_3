package com.github.milomarten.fracktail3.spring.service;

import com.github.milomarten.fracktail3.magic.platform.Person;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * A Role resolver which acts only on a specific type of Person
 * @param <T> The Person class to act on
 */
@Data
public abstract class PlatformSpecificRoleService<T extends Person> {
    private final Class<T> clazz;

    Mono<Set<String>> getRolesForGenericPerson(Person person) {
        return getRolesForPerson((T)person);
    }

    /**
     * Get the roles for this specific Person type
     * @param person The person to get the role of
     * @return The set of roles this user has
     */
    public abstract Mono<Set<String>> getRolesForPerson(T person);

    Mono<Boolean> hasRoleForGenericPerson(Person person, String role) {
        return hasRole((T)person, role);
    }

    /**
     * Check if this person has the specified role
     * @param person The person to check
     * @param role The role to look for
     * @return Asynchronous true if the user has the role, false otherwise
     */
    public Mono<Boolean> hasRole(T person, String role) {
        return getRolesForPerson(person).map(set -> set.contains(role));
    }
}
