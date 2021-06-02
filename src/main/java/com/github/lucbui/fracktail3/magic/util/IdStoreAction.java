package com.github.lucbui.fracktail3.magic.util;

import com.github.lucbui.fracktail3.magic.Id;
import lombok.Data;

@Data
public abstract class IdStoreAction<T extends Id> {
    private final ActionType actionType;
}
