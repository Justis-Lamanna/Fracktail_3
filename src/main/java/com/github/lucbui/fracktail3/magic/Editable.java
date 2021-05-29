package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.params.EntryField;

import java.util.List;

/**
 * An interface which marks some item as editable
 */
public interface Editable {
    /**
     * Edit this item using the provided values
     * @param spec Key/value pairs provided in the edit functionality
     */
    void edit(Spec spec);

    /**
     * Get the fields that are supported by this editable object
     * @return A list of fields that are editable
     */
    List<EntryField> getFields();
}
