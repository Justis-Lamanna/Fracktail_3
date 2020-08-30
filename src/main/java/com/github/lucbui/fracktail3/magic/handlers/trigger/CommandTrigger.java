package com.github.lucbui.fracktail3.magic.handlers.trigger;

public class CommandTrigger extends BaseTrigger {
    public static final CommandTrigger DEFAULT = new CommandTrigger(true, null);

    public CommandTrigger(boolean enabled, String role) {
        super(enabled, role);
    }
}
