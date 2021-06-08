package com.github.milomarten.fracktail3.magic.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.regex.Pattern;

/**
 * Allows for further refining of a String via an enforced regex
 */
@Getter
public class RegexLimit extends ClassLimit {
    @JsonIgnore
    private final Pattern regex;

    /**
     * Create this limit based on a compiled Regex
     * @param regex The regex to use
     */
    public RegexLimit(Pattern regex) {
        super(TypeDescriptor.valueOf(String.class));
        this.regex = regex;
    }

    /**
     * Create this limit based on a raw Regex
     * @param regex The regex to use
     */
    public RegexLimit(String regex) {
        super(TypeDescriptor.valueOf(String.class));
        this.regex = Pattern.compile(regex);
    }

    /**
     * Get the raw Regex pattern
     * @return The raw pattern
     */
    public String getRegexPattern() {
        return regex.pattern();
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj)
                && regex.matcher((String)obj).matches();
    }
}
