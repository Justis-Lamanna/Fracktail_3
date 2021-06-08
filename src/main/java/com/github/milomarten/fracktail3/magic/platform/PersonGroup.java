package com.github.milomarten.fracktail3.magic.platform;

import reactor.core.publisher.Mono;

/**
 * An interface which marks an object as representing a group of Persons
 */
public interface PersonGroup {
    /**
     * Test if this person is in this group
     * @param person The person to test for
     * @return Async true, if in the group, otherwise false
     */
    Mono<Boolean> isInGroup(Person person);
}
