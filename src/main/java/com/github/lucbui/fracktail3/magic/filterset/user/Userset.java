package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.filterset.AbstractComplexFilterSetValidator;

/**
 * A userset, which is specifically for filtering users
 */
public abstract class Userset extends AbstractComplexFilterSetValidator {
    /**
     * Default contructor for Userset
     * @param name name of userset
     * @param blacklist if true, negation is used
     * @param extendsRoleset If non-null, this role extends the specified one.
     */
    public Userset(String name, boolean blacklist, String extendsRoleset) {
        super(name, blacklist, extendsRoleset);
    }

    /**
     * Default constructor for Userset, not using any negation or extension
     * @param name The name of the set.
     */
    public Userset(String name) {
        this(name, false, null);
    }
}
