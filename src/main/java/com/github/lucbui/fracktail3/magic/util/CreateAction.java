package com.github.lucbui.fracktail3.magic.util;

import com.github.lucbui.fracktail3.magic.Id;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class CreateAction<T extends Id> extends IdStoreAction<T> {
    private final T item;

    public CreateAction(T item) {
        super(ActionType.CREATE);
        this.item = item;
    }

    public String getId() {
        return item.getId();
    }
}
