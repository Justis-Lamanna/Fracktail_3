package com.github.lucbui.fracktail3.magic.params;

import org.apache.commons.lang3.StringUtils;

public class NotBlankLimit extends ClassLimit {
    public NotBlankLimit() {
        super(String.class);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && StringUtils.isNotBlank((String)obj);
    }
}
