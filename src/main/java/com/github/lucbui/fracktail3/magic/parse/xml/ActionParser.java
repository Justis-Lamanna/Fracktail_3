package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.xsd.DTDAction;
import com.github.lucbui.fracktail3.xsd.DTDBehavior;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;

public interface ActionParser {
    Action fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior, DTDAction action);
}
