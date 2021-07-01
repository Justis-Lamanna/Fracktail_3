package com.github.milomarten.fracktail3.modules.cc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CCRequest extends CCRequestModel {
    private final long id;

    public CCRequest(long id, CCRequestModel request) {
        super(request.getCode(), request.getViewer(), request.getType());
        this.id = id;
    }
}
