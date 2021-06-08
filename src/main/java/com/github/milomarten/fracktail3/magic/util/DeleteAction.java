package com.github.milomarten.fracktail3.magic.util;

import com.github.milomarten.fracktail3.magic.Id;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class DeleteAction<T extends Id> extends IdStoreAction<T> {
    private final T item;

    public DeleteAction(T item) {
        super(ActionType.DELETE);
        this.item = item;
    }

    public String getId() {
        return item.getId();
    }
}
