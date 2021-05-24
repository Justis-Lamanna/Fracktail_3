package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.platform.Person;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
public class CompositeRoleService implements RoleService {
    private final List<PlatformSpecificRoleService<? extends Person>> platformSpecificRoleServices;

    @Override
    public Mono<Set<String>> getRolesForPerson(Person person) {
        for(PlatformSpecificRoleService<? extends Person> service : platformSpecificRoleServices) {
            if(service.getClazz().isInstance(person)) {
                return service.getRolesForGenericPerson(person);
            }
        }
        return Mono.just(Collections.emptySet());
    }

    @Override
    public Mono<Boolean> hasRole(Person person, String role) {
        for(PlatformSpecificRoleService<? extends Person> service : platformSpecificRoleServices) {
            if(service.getClazz().isInstance(person)) {
                return service.hasRoleForGenericPerson(person, role);
            }
        }
        return Mono.just(false);
    }
}
