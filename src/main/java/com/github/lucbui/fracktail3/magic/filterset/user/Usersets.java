package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Validated;

import java.util.*;

/**
 * A mapping of all usersets in the bot.
 */
public class Usersets implements Validated {
    private final Map<String, Userset> usersets;

    /**
     * Create usersets from a name-userset map.
     * @param usersets The map to use.
     */
    public Usersets(Map<String, Userset> usersets) {
        this.usersets = Collections.unmodifiableMap(usersets);
    }

    /**
     * Get a userset, if it exists
     * @param name The name of the userset
     * @return The matching userset, if present.
     */
    public Optional<Userset> getUserset(String name) {
        return Optional.ofNullable(usersets.get(name));
    }

    @Override
    public void validate(BotSpec spec) throws BotConfigurationException {
        for(Userset set : usersets.values()) {
            if(set.getExtends().isPresent() && !usersets.containsKey(set.getExtends().get())) {
                throw new BotConfigurationException("Role " + set.getName() + " extends unknown role " + set.getExtends().get());
            }

            validateNonRecursiveExtends(usersets, Collections.singleton(set.getName()), set);
        }
    }

    private void validateNonRecursiveExtends(Map<String, Userset> roles, Set<String> encounteredRolesets, Userset userset) {
        if(userset.getExtends().isPresent()) {
            Userset extension = roles.get(userset.getExtends().get());
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
