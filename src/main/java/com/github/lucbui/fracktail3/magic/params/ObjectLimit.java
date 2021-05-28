package com.github.lucbui.fracktail3.magic.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * A Limit which indicates an object is expected
 * This solely works on Map instances right now only. Maybe one day it will work for any object.
 */
@Getter
public class ObjectLimit extends ClassLimit {
    private static final TypeDescriptor MAP_STRING_OBJECT_TYPE =
            TypeDescriptor.map(Map.class, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Object.class));

    private final Map<String, TypeLimits> fields;

    /**
     * Initialize this limit with the acceptable fields
     * @param fields The fields and their individual limits
     */
    public ObjectLimit(Map<String, TypeLimits> fields) {
        super(MAP_STRING_OBJECT_TYPE);
        this.fields = fields;
    }

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

    /**
     * Builder class to make it easier to construct ObjectLimits
     */
    public static class Builder {
        private final Map<String, TypeLimits> fields = new HashMap<>();

        /**
         * Add a field to the constructed limit
         * @param field The name of the field
         * @param limits The type and limits of that individual field
         * @return This builder
         */
        public Builder withField(String field, TypeLimits limits) {
            this.fields.put(field, limits);
            return this;
        }

        /**
         * Create the ObjectLimit
         * @return The created ObjectLimit
         */
        public ObjectLimit build() {
            return new ObjectLimit(this.fields);
        }
    }
}
