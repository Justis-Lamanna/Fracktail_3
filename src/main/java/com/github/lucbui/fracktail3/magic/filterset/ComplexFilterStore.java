package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Validated;

import java.util.*;

public class ComplexFilterStore<ITEM extends AbstractIdFilter> implements Validated {
    private final Map<String, ITEM> store;


    public ComplexFilterStore(Map<String, ITEM> store) {
        this.store = Collections.unmodifiableMap(store);
    }

    public Optional<ITEM> getById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void validate(BotSpec spec) throws BotConfigurationException {
        for(ITEM set : store.values()) {
            if(set.getExtends().isPresent() && !store.containsKey(set.getExtends().get())) {
                throw new BotConfigurationException("Role " + set.getName() + " extends unknown role " + set.getExtends().get());
            }

            validateNonRecursiveExtends(store, Collections.singleton(set.getName()), set);
        }
    }

    private void validateNonRecursiveExtends(Map<String, ITEM> roles, Set<String> encounteredRolesets, ITEM userset) {
        if(userset.getExtends().isPresent()) {
            ITEM extension = roles.get(userset.getExtends().get());
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
