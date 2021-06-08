package com.github.milomarten.fracktail3.magic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.milomarten.fracktail3.magic.params.EntryField;

import java.util.List;

/**
 * An interface which marks some item as editable
 */
public interface Editable<THIS> {
    /**
     * Edit this item using the provided values
     * @param spec Key/value pairs provided in the edit functionality
     */
    THIS edit(GenericSpec spec);

    /**
     * Get the fields that are supported by this editable object
     * @return A list of fields that are editable
     */
    @JsonIgnore
    List<EntryField> getEditFields();

    /**
     * Check if this editable is editable
     * @return True, if this editable is editable, false if locked
     */
    default boolean isEditable() {
        return true;
    }

    /**
     * Test if an arbitrary object is editable
     * @param obj
     * @return
     */
    static boolean isEditable(Object obj) {
        return obj instanceof Editable && ((Editable<?>)obj).isEditable();
    }
}
