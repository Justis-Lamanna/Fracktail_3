package com.github.lucbui.fracktail3.magic;

/**
 * Allows a class to perform additional configurations when created by a BotCreator
 */
public interface BotCreatorAware {
    /**
     * Perform additional configurations on the BotCreator
     * @param creator The creator currently being used.
     */
    void configure(BotCreator creator);
}
