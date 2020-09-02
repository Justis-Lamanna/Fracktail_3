package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Usersets {
    private final Map<String, Userset> rolesets;

    public Usersets(Map<String, Userset> rolesets) {
        this.rolesets = Collections.unmodifiableMap(rolesets);
    }

    public Map<String, Userset> getUsersets() {
        return rolesets;
    }

    public Optional<Userset> getUserset(String name) {
        return Optional.ofNullable(rolesets.get(name));
    }

    public void validate() throws BotConfigurationException {
        for(Userset set : rolesets.values()) {
            if(StringUtils.isNotBlank(set.getExtends()) && !rolesets.containsKey(set.getExtends())) {
                throw new BotConfigurationException("Role " + set.getName() + " extends unknown role " + set.getExtends());
            }

            validateNonRecursiveExtends(rolesets, Collections.singleton(set.getName()), set);
        }
    }

    private void validateNonRecursiveExtends(Map<String, Userset> roles, Set<String> encounteredRolesets, Userset userset) {
        if(StringUtils.isNotBlank(userset.getExtends())) {
            Userset extension = roles.get(userset.getExtends());
            if(extension != null){
                if(encounteredRolesets.contains(extension.getName())) {
                    String chain = String.join("->", encounteredRolesets);
                    throw new BotConfigurationException("Circular dependency detected for roleset: " + chain + "->" + userset.getExtends());
                } else {
                    HashSet<String> newSet = new LinkedHashSet<>(encounteredRolesets);
                    newSet.add(extension.getName());
                    validateNonRecursiveExtends(roles, newSet, extension);
                }
            }
        }
    }
}
