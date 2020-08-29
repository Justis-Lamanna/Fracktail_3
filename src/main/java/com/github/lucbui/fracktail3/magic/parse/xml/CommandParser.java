package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommandWithId;

public interface CommandParser {
    Command fromXml(DTDBot xml, DTDCommandWithId command);
    BehaviorParser getBehaviorParser();
    ActionParser getActionParser();
}
