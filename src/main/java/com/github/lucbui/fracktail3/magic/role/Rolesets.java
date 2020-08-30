package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Rolesets {
    private final Map<String, Roleset> rolesets;

    public Rolesets(Map<String, Roleset> rolesets) {
        this.rolesets = Collections.unmodifiableMap(rolesets);
    }

    public Map<String, Roleset> getRolesets() {
        return rolesets;
    }

    public Optional<Roleset> getRoleset(String name) {
        return Optional.ofNullable(rolesets.get(name));
    }

    public void validate() throws BotConfigurationException {
        for(Roleset set : rolesets.values()) {
            if(StringUtils.isNotBlank(set.getExtends()) && !rolesets.containsKey(set.getExtends())) {
                throw new BotConfigurationException("Role " + set.getName() + " extends unknown role " + set.getExtends());
            }

            validateNonRecursiveExtends(rolesets, Collections.singleton(set.getName()), set);
        }
    }

    private void validateNonRecursiveExtends(Map<String, Roleset> roles, Set<String> encounteredRolesets, Roleset roleset) {
        if(StringUtils.isNotBlank(roleset.getExtends())) {
            Roleset extension = roles.get(roleset.getExtends());
            if(extension != null){
                if(encounteredRolesets.contains(extension.getName())) {
                    String chain = String.join("->", encounteredRolesets);
                    throw new BotConfigurationException("Circular dependency detected for roleset: " + chain + "->" + roleset.getExtends());
                } else {
                    HashSet<String> newSet = new LinkedHashSet<>(encounteredRolesets);
                    newSet.add(extension.getName());
                    validateNonRecursiveExtends(roles, newSet, extension);
                }
            }
        }
    }
}
