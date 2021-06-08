package com.github.milomarten.fracktail3.magic.params;

import java.util.regex.Pattern;

public class EmailLimit extends ClassLimit {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9!#$%&'*+\\-/=?^_`{|}~.]+@[A-Za-z0-9-.]+");

    public EmailLimit() {
        super(String.class);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && EMAIL_PATTERN.matcher((String)obj).matches();
    }
}
