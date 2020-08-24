package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.xsd.DTDBehavior;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;

public interface BehaviorParser {
    Behavior fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior);
    ActionParser getActionParser();
}
