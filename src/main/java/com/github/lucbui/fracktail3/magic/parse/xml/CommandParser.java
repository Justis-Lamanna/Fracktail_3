package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;

public interface CommandParser {
    Command fromXml(DTDBot xml, DTDCommand command);
    BehaviorParser getBehaviorParser();
    ActionParser getActionParser();
}
