package com.github.lucbui.fracktail3.magic.command.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.regex.Pattern;

@Getter
public class RegexLimit extends ClassLimit {
    private final Pattern regex;

    public RegexLimit(Pattern regex) {
        super(TypeDescriptor.valueOf(String.class));
        this.regex = regex;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj)
                && regex.matcher((String)obj).matches();
    }
}
