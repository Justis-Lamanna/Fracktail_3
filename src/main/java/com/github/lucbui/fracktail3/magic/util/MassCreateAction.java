package com.github.lucbui.fracktail3.magic.util;

import com.github.lucbui.fracktail3.magic.Id;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString(callSuper = true)
public class MassCreateAction<T extends Id> extends IdStoreAction<T> {
    private final Collection<T> items;

    public MassCreateAction(Collection<T> items) {
        super(ActionType.CREATE);
        this.items = items;
    }
}
