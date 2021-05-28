package com.github.lucbui.fracktail3.magic.command.params;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ObjectLimit implements TypeLimits {
    private final Map<String, TypeLimits> fields;

    @Override
    public boolean matches(Object obj) {
        if(obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            return map.keySet()
                    .stream()
                    .allMatch(key -> fields.get(key).matches(map.get(key)));
        } else {
            return false;
        }
    }

    public static class Builder {
        private final Map<String, TypeLimits> fields = new HashMap<>();

        public Builder withField(String field, TypeLimits limits) {
            this.fields.put(field, limits);
            return this;
        }

        public ObjectLimit build() {
            return new ObjectLimit(this.fields);
        }
    }
}
