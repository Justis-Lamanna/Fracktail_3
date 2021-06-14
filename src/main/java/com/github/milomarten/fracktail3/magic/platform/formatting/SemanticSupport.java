package com.github.milomarten.fracktail3.magic.platform.formatting;

/**
 * Indicates that a platform supports semantic meaning
 */
public interface SemanticSupport {
    /**
     * Get the formatting that should be used for the provided intent
     * @param intent The intent to get the formatting for
     * @return The Formatting object which describes how to handle the intent
     */
    Formatting forIntent(Intent intent);
}
