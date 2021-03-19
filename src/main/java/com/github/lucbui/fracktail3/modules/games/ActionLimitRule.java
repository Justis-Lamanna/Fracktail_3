package com.github.lucbui.fracktail3.modules.games;

import org.apache.commons.lang3.ArrayUtils;

public class ActionLimitRule<GF> implements Rule<GF> {
    private final Class<? extends Action<GF>>[] permissableActions;

    @SafeVarargs
    public ActionLimitRule(Class<? extends Action<GF>>... permissableActions) {
        this.permissableActions = permissableActions;
    }

    @Override
    public ActionLegality isLegalMove(Action<GF> action, GF board) {
        return ActionLegality.test(ArrayUtils.contains(permissableActions, action.getClass()), "Illegal action attempted");
    }
}
