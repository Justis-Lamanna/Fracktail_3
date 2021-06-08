package com.github.milomarten.fracktail3.spring.service;

import com.github.milomarten.fracktail3.magic.platform.Person;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Get the roles a person has. For use in role-based command limiting
 */
public interface RoleService {
    /**
     * Get the roles for a given person
     * @param person The person to look up
     * @return The roles, determined asynchronously
     */
    Mono<Set<String>> getRolesForPerson(Person person);

    /**
     * Check if the user has a specific role
     * @param person The person to look up
     * @param role The role to check for
     * @return Asynchronous true, if the roles are present, or false otherwise.
     */
    default Mono<Boolean> hasRole(Person person, String role) {
        return getRolesForPerson(person).map(set -> set.contains(role));
    }
}
