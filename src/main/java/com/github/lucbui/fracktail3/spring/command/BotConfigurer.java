package com.github.lucbui.fracktail3.spring.command;

/**
 * Plugin to allow for plug-and-play bot configuration
 */
public interface BotConfigurer {
    /**
     * Configure the bot given the provided bean
     * @param bean The proved bean
     * @param name
     */
    void configure(Object bean, String name);
}
