package com.github.milomarten.fracktail3.magic.util;

import com.github.milomarten.fracktail3.magic.Id;
import lombok.Data;

@Data
public abstract class IdStoreAction<T extends Id> {
    private final ActionType actionType;
}
