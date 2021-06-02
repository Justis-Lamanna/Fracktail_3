package com.github.lucbui.fracktail3.magic.util;

import com.github.lucbui.fracktail3.magic.Id;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
public class UpdateAction<T extends Id> extends IdStoreAction<T> {
    private final T newItem;
    private final T oldItem;

    public UpdateAction(T newItem, T oldItem) {
        super(ActionType.UPDATE);
        Assert.isTrue(Objects.equals(newItem.getId(), oldItem.getId()), "newItem and oldItem don't share IDs");
        this.newItem = newItem;
        this.oldItem = oldItem;
    }

    public String getId() {
        return newItem.getId();
    }
}
