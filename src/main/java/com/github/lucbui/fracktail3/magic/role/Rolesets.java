package com.github.lucbui.fracktail3.magic.role;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Rolesets {
    private final Map<String, Roleset> rolesets;

    public Rolesets(Map<String, Roleset> rolesets) {
        this.rolesets = Collections.unmodifiableMap(rolesets);
    }

    public Optional<Roleset> getRoleset(String name) {
        return Optional.ofNullable(rolesets.get(name));
    }
}
