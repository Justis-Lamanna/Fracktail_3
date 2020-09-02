package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;
import com.github.lucbui.fracktail3.xsd.DTDCommandAction;

public interface ActionParser {
    Action fromXml(DTDBot xml, DTDCommand command, DTDCommandAction action);
}
