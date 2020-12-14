package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;

/**
 * A piece of a MethodCallingAction or FieldCallingAction which determines how the method's response should be handled
 */
public class ReturnComponent extends ReturnBaseComponent<CommandUseContext<?>> {
    public ReturnComponent(ReturnConverterFunction<? super CommandUseContext<?>> func) {
        super(func);
    }
}
